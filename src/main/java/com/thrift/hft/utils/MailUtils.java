package com.thrift.hft.utils;


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
}
