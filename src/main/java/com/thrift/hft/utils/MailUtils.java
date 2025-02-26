package com.thrift.hft.utils;


import com.thrift.hft.entity.Product;
import com.thrift.hft.exceptions.MailServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Component
public class MailUtils {

    private static final Logger logger = LogManager.getLogger(MailUtils.class);

    private static String from;

    private static JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    public void setFrom(String from) {
        MailUtils.from = from;
    }

    @Autowired
    public MailUtils(JavaMailSender mailSender) {
        MailUtils.mailSender = mailSender;
    }

    public static void send(String to, String mailSubject, String mailBody, String attachment) throws MessagingException {

        logger.info("MailUtils - Inside send method");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        try {
            mimeMessageHelper.setSubject(mailSubject);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(mailBody, Boolean.TRUE);

            if (attachment != null){
                FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
                mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new MailServerException();
        }
    }

   public static String generateEmailContent(String userName, String orderId, String orderDate, BigDecimal totalAmount, List<Product> products) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<html><body>");
        emailBody.append("<h2>Thank You for Your Order!</h2>");
        emailBody.append("<p>Hi ").append(userName).append(",</p>");
        emailBody.append("<p>We appreciate your purchase. Below are the details of your order:</p>");
        emailBody.append("<p><strong>Order ID:</strong> ").append(orderId).append("</p>");
        emailBody.append("<p><strong>Date:</strong> ").append(orderDate).append("</p>");
        emailBody.append("<p><strong>Total Amount:</strong> Rs ").append(totalAmount).append("</p>");
        emailBody.append("<h3>Ordered Items:</h3>");

        for (Product product : products) {
            emailBody.append("<div><p><strong>Product:</strong> ").append(product.getDescription()).append("</p>");
            emailBody.append("<p><strong>Brand:</strong> ").append(product.getBrand()).append("</p>");
            emailBody.append("<p><strong>Price:</strong> Rs ").append(product.getPrize()).append("</p></div>");
        }

        emailBody.append("<p>Your order will be delivered soon. If you have any questions, feel free to contact us.</p>");
        emailBody.append("<p>Thank you for shopping with us!</p>");
        emailBody.append("</body></html>");

        return emailBody.toString();
    }
}
