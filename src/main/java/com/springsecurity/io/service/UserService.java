package com.springsecurity.io.service;

import com.springsecurity.io.entity.Users;
import com.springsecurity.io.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users register(Users users){
      return  userRepository.save(users);
    }
}
