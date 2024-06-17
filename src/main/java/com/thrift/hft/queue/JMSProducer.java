package com.thrift.hft.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JMSProducer {

    private static final Logger logger = LoggerFactory.getLogger(JMSProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;


//    public void createZohoRequest(SendZohoRequestInQueue sendZohoRequestInQueue){
//        try {
//            logger.info("JMSProducer - Inside createZohoRequest method");
//            jmsTemplate.convertAndSend(createZohoRequest, sendZohoRequestInQueue);
//        } catch (Exception e) {
//            logger.error("Received Exception during send Message: ", e);
//        }
//    }
}
