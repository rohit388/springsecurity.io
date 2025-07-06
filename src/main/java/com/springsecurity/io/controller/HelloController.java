package com.springsecurity.io.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getController(HttpServletRequest request){
        return "HelloController"+ request.getSession().getId();
    }
}
