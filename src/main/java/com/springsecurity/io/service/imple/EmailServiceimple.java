package com.springsecurity.io.service.imple;

import com.springsecurity.io.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceimple implements EmailService {


    private final JavaMailSender mailSender;

    public EmailServiceimple(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Your Otp Code");
        mailMessage.setText("Your otp code is :"+ otp);
        mailSender.send(mailMessage);
    }
}
