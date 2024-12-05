package com.bt.ecommerce.messaging;

import com.bt.ecommerce.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailComponent {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${from.email.address}")
    private String fromEmail;

    public void sendEmailUsingGmail(String to, String subject, String body) throws BadRequestException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(fromEmail);
            mailSender.send(message);
        } catch (Exception e) {
            throw new BadRequestException("Failed to send email: " + e.getMessage());
        }
    }

}
