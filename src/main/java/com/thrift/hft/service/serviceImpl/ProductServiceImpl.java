package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.dto.ProductDTO;
import com.thrift.hft.entity.BatchDetails;
import com.thrift.hft.entity.ProductImage;
import com.thrift.hft.entity.Product;
import com.thrift.hft.entity.User;
import com.thrift.hft.enums.ApprovalStatus;
import com.thrift.hft.enums.Role;
import com.thrift.hft.exceptions.AlreadyExistsException;
import com.thrift.hft.exceptions.InvalidException;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.filter.FilterBuilder;
import com.thrift.hft.properties.DocumentPath;
import com.thrift.hft.queue.JMSProducer;
import com.thrift.hft.repository.BatchDetailsRepository;
import com.thrift.hft.repository.ProdImageRepository;
import com.thrift.hft.repository.ProductRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.GetAllProductRequest;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.response.GellAllProductResponse;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.service.RedisService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.UploadDocumentsUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    BatchDetailsRepository batchDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UploadDocumentsUtils uploadDocumentsUtils;

    @Autowired
    private DocumentPath documentPath;
    @Autowired
    private ProdImageRepository prodImageRepository;

    @Autowired
    private RedisService redisService;
    // Assigning weights to different attributes to indicate their importance
    private static final double CATEGORY_WEIGHT = 0.2;
    private static final double BRAND_WEIGHT = 0.2;
    private static final double SIZE_WEIGHT = 0.1;
    private static final double CONDITION_WEIGHT = 0.1;
    private static final double COLOUR_WEIGHT = 0.1;
    private static final double PRICE_WEIGHT = 0.3;



    @Override
    public Page<ProductDTO> getAllProduct(GetAllProductRequest getAllProductRequest) throws IOException {
        logger.info("ProductServiceImpl - Inside getAllProduct method");

        List<Product> allProducts = getAllProducts();

        List<Product> filteredProducts = allProducts.stream()
                .filter(product ->
                        (getAllProductRequest.getCategory() == null || product.getCategory().equals(getAllProductRequest.getCategory())) &&
                                (getAllProductRequest.getSubCategory() == null || product.getSubCategory().equals(getAllProductRequest.getSubCategory())) &&
                                (getAllProductRequest.getBrand() == null || product.getBrand().equals(getAllProductRequest.getBrand())) &&
                                (getAllProductRequest.getProdStatus() == null || product.getProdStatus().equals(getAllProductRequest.getProdStatus())) &&
                                (getAllProductRequest.getApprovalStatus() == null || product.getApprovalStatus().equals(getAllProductRequest.getApprovalStatus())) &&
                                (getAllProductRequest.getSize() == null || product.getSize().equals(getAllProductRequest.getSize())) &&
                                (getAllProductRequest.getColour() == null || product.getColour().equals(getAllProductRequest.getColour())) &&
                                (getAllProductRequest.getCondition() == null || product.getCondition().equals(getAllProductRequest.getCondition()))
                )
                .collect(Collectors.toList());

//        Specification<Product> specification = new FilterBuilder<Product>()
//                .equals("condition",getAllProductRequest.getCondition())
//                .equals("category", getAllProductRequest.getCategory())
//                .equals("subCategory",getAllProductRequest.getSubCategory())
//                .equals("brand", getAllProductRequest.getBrand())
//                .equals("prodStatus",getAllProductRequest.getProdStatus())
//                .equals("approvalStatus", getAllProductRequest.getApprovalStatus())
//                .equals("size", getAllProductRequest.getSize())
//                .equals("colour", getAllProductRequest.getColour())
//                .build();

        int start = (int) getAllProductRequest.getPageable().getOffset();
        int end = Math.min((start + getAllProductRequest.getPageable().getPageSize()), filteredProducts.size());
        List<ProductDTO> pagedProducts = filteredProducts.subList(start, end).stream().map(p-> {
            try {
                return p.getProductDTO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());


        // Step 4: Return the paginated list as a Page object
        return new PageImpl<>(pagedProducts, getAllProductRequest.getPageable(), filteredProducts.size());

//        Page<Product> productPage = productRepository.findAll(specification,getAllProductRequest.getPageable());
//        return productPage.map(product -> {
//            try {
//                return product.getProductDTO();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    @Override
    public ProductDTO viewProduct(String  pid) throws IOException {
        logger.info("ProductServiceImpl - Inside getAllProduct method");
        Product product = productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
        return product.getProductDTO();
    }

    @Override
    public ProductDTO createSellRequest(ProductRequest pr, TokenResponse tokenResponse) throws IOException {
        logger.info("ProductServiceImpl - Inside createSellRequest method");
        if (pr.getFiles() == null){
            throw new InvalidException("Please upload at least two images for the product");
        }
        if (pr.getFiles().length>4)
            throw new InvalidException("You can upload at most four images per  article");

        //TODO:Siddhesh we might have to add this check
//        for (MultipartFile file: pr.getFiles()){
//            if (checkForDuplicate(file,tokenResponse.getUserId()))
//                throw new AlreadyExistsException("Article with same image already exists");
//        }

        Product product = productRepository.save(new Product(pr.getDescription(), pr.getPrize(), CommonUtils.getCondition(pr.getCondition()),
               CommonUtils.getCategory( pr.getCategory()),CommonUtils.getSubCategory(pr.getSubCategory()), CommonUtils.getBrand(pr.getBrand()), tokenResponse.getUserId(),CommonUtils.getSize( pr.getSize()),CommonUtils.getColor(pr.getColor())));


            for (MultipartFile file : pr.getFiles())
                if (!file.isEmpty()) {
                    String filePath = uploadDocumentsUtils.uploadDocuments(file, documentPath.getProductImages(), product.getBrand().name());
                    String[] parts = filePath.split("/");
                    String fileName = parts[parts.length - 1];
                    prodImageRepository.save(new ProductImage(filePath, fileName, product));
                }

        return product.getProductDTO();
    }

    @Override
    public ProductDTO approveSellRequest(String productId, TokenResponse tokenResponse) throws IOException {
       logger.info("ProductServiceImpl - Inside approveSellRequest method");

       if (!tokenResponse.getAuthority().equals(Role.ADMIN.name()))
           throw new InvalidException("Only admin can approve the sell request");
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty())
            throw new NotFoundException("Product not found");

        Product product  = productOptional.get();
        product.setApprovalStatus(ApprovalStatus.APPROVED);
        productRepository.save(product);

        // Update the Redis cache with the modified product
        GellAllProductResponse productList = redisService.get("productList", GellAllProductResponse.class);
        if (productList != null) {
            List<Product> products = productList.getProductList();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId().equals(productId)) {
                    products.set(i, product); // Update the product in the list
                    break;
                }
            }

            // Save the updated product list back to Redis
            redisService.set("productList", new GellAllProductResponse(products), 3L);
        }
        return product.getProductDTO();
    }


    
    @Override
    public List<ProductDTO> getSimilarProduct(String pid) throws IOException {
        logger.info("ProductServiceImpl -Inside getSimilarProducts Method");
        List<Product> productList = getAllProducts();
        Product product = productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
        List<Product> similarProducts = findSimilarProducts(product, productList);
        return similarProducts.stream().map(p-> {
            try {
                return p.getProductDTO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    public boolean checkForDuplicate(MultipartFile file,Long userId) throws IOException {
        String uploadedImageHash = computeHash(file);

        List<ProductImage> imageList = prodImageRepository.findBySellerId(userId);
        for (ProductImage productImage : imageList) {
            byte[] fileBytes = Files.readAllBytes(Paths.get(productImage.getFilePath()));
            String existingImageHash = DigestUtils.md5Hex(fileBytes);

            if (uploadedImageHash.equals(existingImageHash)) {
                return true;
            }
        }
        return false;
    }

    private String computeHash(MultipartFile file) throws IOException {
        return DigestUtils.md5Hex(file.getInputStream());
    }

    public  List<Product>getAllProducts() {
        logger.info("ProductServiceImpl - Inside getAllProducts method");
//        GellAllProductResponse productList = redisService.get("productList", GellAllProductResponse.class);
//        if (productList != null)
//            return productList.getProductList();
//        else{
//            List<Product> products = productRepository.findAll();
//            if (products!=null){
//                redisService.set("productList",new GellAllProductResponse(products),3L);
//            }
//            return products;
//        }
        return productRepository.findAll();
    }

    public RealVector convertToFeatureVector(Product product) {
        double[] features = new double[6];

        // Normalize each feature into a comparable value (scale/weight)
        features[0] = product.getCategory().ordinal() * CATEGORY_WEIGHT;
        features[1] = product.getBrand().ordinal() * BRAND_WEIGHT;
        features[2] = product.getSize().ordinal() * SIZE_WEIGHT;
        features[3] = product.getCondition().ordinal() * CONDITION_WEIGHT;
        features[4] = product.getColour().ordinal() * COLOUR_WEIGHT;
        features[5] = normalizePrice(product.getPrize()) * PRICE_WEIGHT;

        return new ArrayRealVector(features);
    }

    // Normalize price to a scale of 0-1
    private double normalizePrice(BigDecimal price) {
        BigDecimal maxPrice = new BigDecimal("10000"); // Assuming max price in your system
        return price.divide(maxPrice, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double calculateSimilarity(Product p1, Product p2) {
        RealVector v1 = convertToFeatureVector(p1);
        RealVector v2 = convertToFeatureVector(p2);

        // Cosine similarity
        double dotProduct = v1.dotProduct(v2);
        double normProduct = v1.getNorm() * v2.getNorm();

        return dotProduct / normProduct;
    }

    // Find similar products for a given product
    public List<Product> findSimilarProducts(Product targetProduct, List<Product> allProducts) {
        return allProducts.stream()
                .filter(product -> !product.getId().equals(targetProduct.getId())) // exclude the target product
                .sorted((p1, p2) -> Double.compare(
                        calculateSimilarity(targetProduct, p2),
                        calculateSimilarity(targetProduct, p1)))
                .limit(10) // limiting to top 10 similar products
                .collect(Collectors.toList());
    }




}
