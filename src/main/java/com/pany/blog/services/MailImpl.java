package com.pany.blog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class MailImpl {

    private final JavaMailSenderImpl mailSender;

    @Autowired
    public MailImpl(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject("BLOG");
        mailMessage.setText("Поддтверждение регистрации...");

        mailSender.send(mailMessage);
    }

}
