package com.springsecurity.io.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);
}