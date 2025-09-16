package com.springsecurity.io.service.imple;

import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.entity.Users;
import com.springsecurity.io.exception.ResourceNotFoundException;
import com.springsecurity.io.mapper.UserMapper;
import com.springsecurity.io.model.UserPrincipal;
import com.springsecurity.io.repo.UserRepository;
import com.springsecurity.io.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserServiceImple implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImple(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users users = userRepository.findByEmail(email);
        if(users==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(users);
    }

    @Override
    public Page<UserRequest> getAllUser(Pageable pageable) {
        Page<Users> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    public UserRequest getUser(Long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }
}

