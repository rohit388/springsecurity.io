package com.springsecurity.io.controller;

import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<UserRequest>>getAllUser(Pageable pageable){
        return ResponseEntity.ok(userService.getAllUser(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest>getUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }

}
