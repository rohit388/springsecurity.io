package com.springsecurity.io.controller;

import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserRequest>>getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest>getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }

}
