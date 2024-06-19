package com.thrift.hft.queue;

import com.thrift.hft.service.serviceImpl.utils.async.SendSellRequestInQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JMSProducer {

    private static final Logger logger = LoggerFactory.getLogger(JMSProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${hft.queue.sell-request-queue}")
    private String createSellRequest;


    public void createSellRequest(SendSellRequestInQueue sendSellRequestInQueue){
        try {
            logger.info("JMSProducer - Inside createSellRequest method");
            jmsTemplate.convertAndSend(createSellRequest, sendSellRequestInQueue);
        } catch (Exception e) {
            logger.error("Received Exception during send Message: ", e);
        }
    }
}
