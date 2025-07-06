package com.springsecurity.io.controller;

import com.springsecurity.io.entity.Users;
import com.springsecurity.io.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public Users register(@RequestBody Users users){
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        return userService.register(users);
    }

}
