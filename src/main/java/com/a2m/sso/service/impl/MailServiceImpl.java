package com.a2m.sso.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.a2m.sso.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerifyEmail(String verifyKey, String email){
    	try {
    		MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(new InternetAddress("nctvip19@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Verification for account");

            String htmlContent = "<h1>Thank you for signing up!</h1>" +
            					 "<h2>Please click on the link below to verify your account</h2>" + 
            					 "<a href=\"https://localhost:8097\" style=\"display:inline-block; padding:12px 24px; background-color:#007bff; color:#fff; text-decoration:none;\">Get Started</a>";
            message.setContent(htmlContent, "text/html; charset=utf-8");

            javaMailSender.send(message);
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}
    }
}