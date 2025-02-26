package com.thrift.hft.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface IEmailService {

     void sendOrderConfirmationEmail(String to, Map<String, Object> model) throws MessagingException;
}
