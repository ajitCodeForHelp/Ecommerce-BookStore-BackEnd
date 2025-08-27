package com.bt.ecommerce.messaging;

import com.bt.ecommerce.exception.BadRequestException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailComponent {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${from.email.address}")
    private String fromEmail;

    public void sendEmailUsingGmail(String to, String subject, String body) throws BadRequestException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);
            mailSender.send(message);
            System.out.println("************Email has been sent successfully************");
        } catch (Exception e) {
            System.out.println("************Email not sent************");
            e.printStackTrace();
        }
    }
}
