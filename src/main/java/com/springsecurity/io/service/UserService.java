package com.springsecurity.io.service;

import com.springsecurity.io.config.JwtService;
import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.entity.Users;
import com.springsecurity.io.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    Page<UserRequest> getAllUser(Pageable pageable);
    UserRequest getUser(Long id);
}
