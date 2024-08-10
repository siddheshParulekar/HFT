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
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.UploadDocumentsUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
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


    @Override
    public Page<ProductDTO> getAllProduct(GetAllProductRequest getAllProductRequest) throws IOException {
        logger.info("ProductServiceImpl - Inside getAllProduct method");

        Specification<Product> specification = new FilterBuilder<Product>()
                .equals("condition",getAllProductRequest.getCondition())
                .equals("category", getAllProductRequest.getCategory())
                .equals("subCategory",getAllProductRequest.getSubCategory())
                .equals("brand", getAllProductRequest.getBrand())
                .equals("prodStatus",getAllProductRequest.getProdStatus())
                .equals("approvalStatus", getAllProductRequest.getApprovalStatus())
                .equals("size", getAllProductRequest.getSize())
                .equals("colour", getAllProductRequest.getColour())
                .build();

        Page<Product> productPage = productRepository.findAll(specification,getAllProductRequest.getPageable());
        return productPage.map(product -> {
            try {
                return product.getProductDTO();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

        for (MultipartFile file: pr.getFiles()){
            if (checkForDuplicate(file,tokenResponse.getUserId()))
                throw new AlreadyExistsException("Article with same image already exists");
        }

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

        return product.getProductDTO();
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
}
