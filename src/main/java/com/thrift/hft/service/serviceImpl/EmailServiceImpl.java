package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public void sendOrderConfirmationEmail(String to, Map<String, Object> model) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(model);
        String htmlContent = templateEngine.process("order-confirmation", context);

        helper.setTo(to);
        helper.setSubject("Your Order is Confirmed!");
        helper.setText(htmlContent, true); // true means HTML

        mailSender.send(message);
    }
}
