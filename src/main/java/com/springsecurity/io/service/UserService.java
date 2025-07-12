package com.springsecurity.io.service;

import com.springsecurity.io.config.JwtService;
import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.entity.Users;
import com.springsecurity.io.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public Users register(Users users){
      return  userRepository.save(users);
    }

    public String verify(LoginRequest users) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
        if(authentication.isAuthenticated()){
           return jwtService.generateToken(users.getUsername());
        }
        return "fail";
    }
}
