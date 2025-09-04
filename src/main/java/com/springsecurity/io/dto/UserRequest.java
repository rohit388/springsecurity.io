package com.springsecurity.io.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequest {
    
    private String email;
    private String password;
    private String status;


}
