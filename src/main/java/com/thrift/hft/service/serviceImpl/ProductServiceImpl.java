package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.entity.BatchDetails;
import com.thrift.hft.entity.ProductImage;
import com.thrift.hft.entity.Product;
import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.InvalidException;
import com.thrift.hft.properties.DocumentPath;
import com.thrift.hft.queue.JMSProducer;
import com.thrift.hft.repository.BatchDetailsRepository;
import com.thrift.hft.repository.ProdImageRepository;
import com.thrift.hft.repository.ProductRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.UploadDocumentsUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;
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

    @Override
    public Long createSellRequest(List<ProductRequest> productRequestList, TokenResponse tokenResponse) {
        logger.info("ProductServiceImpl - Inside createSellRequest");
        BatchDetails batchDetails = createBatchDetails(BigDecimal.valueOf(productRequestList.size()), tokenResponse.getUserId());
        saveProduct(productRequestList, batchDetails.getId(), tokenResponse);
        // jmsProducer.createSellRequest(new SendSellRequestInQueue(productRequestList,tokenResponse));
        return batchDetails.getId();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchDetails createBatchDetails(BigDecimal numberOfArticle, Long userId) {
        logger.info("ProductServiceImpl - Inside createBatchDetails");
        User user = userRepository.findById(userId).get();
        if (user.getMobileNumber() == null|| user.getAddress().isEmpty())
                throw new InvalidException("Please fill all user details ");
        return batchDetailsRepository.save(new BatchDetails(numberOfArticle, userId, user.getMobileNumber()));
    }

    public void saveProduct(List<ProductRequest> productRequestList, Long batchId, TokenResponse tokenResponse) {
        logger.info("ProductServiceImpl - Inside saveProduct");

        for (ProductRequest pr : productRequestList) {
            Product product = productRepository.save(new Product(pr.getProductName(), pr.getPrize(), pr.getCondition(),
                    pr.getCategory(), pr.getSubCategory(),  pr.getBrand(), tokenResponse.getUserId(), batchId,pr.getSize()));

            List<Part> images = pr.getImages();
            for (Part file : images) {
                String filePath = uploadDocumentsUtils.uploadDocuments(file, documentPath.getProductImages(), product.getBrand().name());
                String[] parts = filePath.split("/");
                String fileName = parts[parts.length - 1];
                prodImageRepository.save(new ProductImage(filePath, fileName, product));
            }
        }
    }


}