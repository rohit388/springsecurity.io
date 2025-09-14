package com.springsecurity.io.service;

import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.dto.OtpVerificationRequest;
import com.springsecurity.io.dto.PasswordlessLoginRequest;
import com.springsecurity.io.dto.UserRequest;

public interface AuthService {

    UserRequest register(UserRequest userRequest);
    String verify(LoginRequest loginRequest);

    void requestOtp(PasswordlessLoginRequest passwordlessLoginRequest);

    String verifyOtpAndLogin(OtpVerificationRequest otpVerificationRequest);
}

