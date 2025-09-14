package com.springsecurity.io.dto;

import lombok.Data;

@Data
public class PasswordlessLoginRequest {
    private String email;
}
