package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.entity.BatchDetails;
import com.thrift.hft.entity.Product;
import com.thrift.hft.entity.User;
import com.thrift.hft.enums.*;
import com.thrift.hft.queue.JMSProducer;
import com.thrift.hft.repository.BatchDetailsRepository;
import com.thrift.hft.repository.ProductRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.request.SellRequest;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.service.serviceImpl.utils.async.SendSellRequestInQueue;
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
import java.util.Optional;

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

    @Override
    public Long createSellRequest(List<ProductRequest> productRequestList, TokenResponse tokenResponse) {
        logger.info("ProductServiceImpl - Inside createSellRequest");
        logger.info("........" +tokenResponse.getUserId());
        logger.info("ProductRequest ....." + productRequestList);
        BatchDetails batchDetails = createBatchDetails(BigDecimal.valueOf(productRequestList.size()), tokenResponse.getUserId());

       // jmsProducer.createSellRequest(new SendSellRequestInQueue(productRequestList,tokenResponse));
        return batchDetails.getId();
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchDetails createBatchDetails(BigDecimal numberOfArticle, Long userId){
        User user = userRepository.findById(userId).get();
        return batchDetailsRepository.save(new BatchDetails(numberOfArticle,userId,user.getMobileNumber()));
    }

    public void saveProduct(List<ProductRequest> productRequestList,TokenResponse tokenResponse){

        for (ProductRequest pr:productRequestList){
            Product product = productRepository.save(new Product(pr.getProductName(), pr.getPrize(), pr.getCondition(),
                    pr.getCategory(), pr.getSubCategoryM(), pr.getSubCategoryW(), pr.getBrand(), tokenResponse.getUserId()));
            List<Part> images = pr.getImages();
            for (Part file: images){
                uploadDocumentsUtils.uploadDocuments(file,"P")

            }

        }
        productRequestList.stream().forEach(pr-> productRepository.save(new Product(pr.getProductName(),pr.getPrize(),pr.getCondition(),
                pr.getCategory(),pr.getSubCategoryM(),pr.getSubCategoryW(),pr.getBrand(), tokenResponse.getUserId())));
    }



}