package com.springsecurity.io.controller;

import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.dto.OtpVerificationRequest;
import com.springsecurity.io.dto.PasswordlessLoginRequest;
import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRequest> register(@RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> verify(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authService.verify(loginRequest));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestOtp(@Valid @RequestBody PasswordlessLoginRequest passwordlessLoginRequest) {
        authService.requestOtp(passwordlessLoginRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtpAndLogin(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        String token = authService.verifyOtpAndLogin(otpVerificationRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }
}

