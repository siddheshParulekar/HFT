package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.dto.ProductDTO;
import com.thrift.hft.entity.BatchDetails;
import com.thrift.hft.entity.ProductImage;
import com.thrift.hft.entity.Product;
import com.thrift.hft.entity.User;
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
import com.thrift.hft.utils.UploadDocumentsUtils;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    BatchDetailsRepository batchDetailsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JMSProducer jmsProducer;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UploadDocumentsUtils uploadDocumentsUtils;

    @Autowired
    private DocumentPath documentPath;
    @Autowired
    private ProdImageRepository prodImageRepository;

//    @Override
//    public Long createSellRequest(List<ProductRequest> productRequestList, TokenResponse tokenResponse) {
//        logger.info("ProductServiceImpl - Inside createSellRequest");
//        BatchDetails batchDetails = createBatchDetails(BigDecimal.valueOf(productRequestList.size()), tokenResponse.getUserId());
//        saveProduct(productRequestList, batchDetails.getId(), tokenResponse);
//        // jmsProducer.createSellRequest(new SendSellRequestInQueue(productRequestList,tokenResponse));
//        return batchDetails.getId();
//    }


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
    public ProductDTO viewProduct(Long pid) throws IOException {
        logger.info("ProductServiceImpl - Inside getAllProduct method");
        Product product = productRepository.findById(pid).orElseThrow(() -> new NotFoundException("Product not found"));
        return product.getProductDTO();
    }

    @Override
    public Long createSellRequest(ProductRequest request, TokenResponse tokenResponse) {

        return 0L;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchDetails createBatchDetails(BigDecimal numberOfArticle, Long userId) {
        logger.info("ProductServiceImpl - Inside createBatchDetails");
        User user = userRepository.findById(userId).get();
//        if (user.getMobileNumber() == null|| user.getAddress() == null)
//                throw new InvalidException("Please fill all user details ");
        return batchDetailsRepository.save(new BatchDetails(numberOfArticle, userId, user.getMobileNumber()));
    }

    public void saveProduct(List<ProductRequest> productRequestList, Long batchId, TokenResponse tokenResponse) {
        logger.info("ProductServiceImpl - Inside saveProduct");

        for (ProductRequest pr : productRequestList) {
            Product product = productRepository.save(new Product(pr.getProductName(), pr.getPrize(), pr.getCondition(),
                    pr.getCategory(), pr.getSubCategory(),  pr.getBrand(), tokenResponse.getUserId(), batchId,pr.getSize()));

//            List<Part> images = pr.getImages();
            for (MultipartFile file : pr.getImages()) {
                String filePath = uploadDocumentsUtils.uploadDocuments(file, documentPath.getProductImages(), product.getBrand().name());
                String[] parts = filePath.split("/");
                String fileName = parts[parts.length - 1];
                prodImageRepository.save(new ProductImage(filePath, fileName, product));
            }
        }
    }


}