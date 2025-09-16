package com.springsecurity.io.dto;

import lombok.Data;

@Data
public class OtpVerificationRequest {

    private String email;
    private String otp;
}
