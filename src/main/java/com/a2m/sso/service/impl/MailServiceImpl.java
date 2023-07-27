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

    public void sendVerifyEmail(String verifyKey, String email, String redirectUri, String func){
    	try {
    		MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(new InternetAddress("nctvip19@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Verification for account");
            if (func.equals("verify")) {
            	String link = "http://localhost:8097/auth/verify?verifyKey=" + verifyKey + "&redirectUri=" + redirectUri;
                String htmlContent = "<h1>Thank you for signing up!</h1>" +
                					 "<h2>Please click on the link below to verify your account</h2>" + 
                					 "<a href=\"" + link + "\" style=\"display: inline-block; padding: 12px 24px; background-color: #007bff; color: #fff; text-decoration: none;\">\n" +
                                             "Get Started\n" +
                                             "</a>\n";
                message.setContent(htmlContent, "text/html; charset=utf-8");
            }
            else if (func.equals("forgot")) {
            	String link = "http://localhost:8097/auth/reset?verifyKey=" + verifyKey + "&redirectUri=" + redirectUri;
                String htmlContent = "<h2>Please click on the link below to reset your password</h2>" + 
                					 "<a href=\"" + link + "\" style=\"display: inline-block; padding: 12px 24px; background-color: #007bff; color: #fff; text-decoration: none;\">\n" +
                                             "Get Started\n" +
                                             "</a>\n";
                message.setContent(htmlContent, "text/html; charset=utf-8");
            }

            javaMailSender.send(message);
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}
    }
}