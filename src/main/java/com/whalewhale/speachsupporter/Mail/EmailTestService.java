package com.whalewhale.speachsupporter.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailTestService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("your-email@example.com");
        message.setSubject("Test Email");
        message.setText("This is a test email from Spring Boot.");
        mailSender.send(message);
    }
}