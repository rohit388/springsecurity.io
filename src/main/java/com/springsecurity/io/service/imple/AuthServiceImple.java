package com.springsecurity.io.service.imple;

import com.springsecurity.io.config.JwtService;
import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.entity.Role;
import com.springsecurity.io.entity.Users;
import com.springsecurity.io.mapper.UserMapper;
import com.springsecurity.io.repo.RoleRepository;
import com.springsecurity.io.repo.UserRepository;
import com.springsecurity.io.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImple implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public AuthServiceImple(AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public UserRequest register(UserRequest userRequest) {
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        Users users = userMapper.toEntity(userRequest);
        
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found!"));
        users.getRoles().add(defaultRole);
        
        Users user = userRepository.save(users);
        return userMapper.toDto(user);
    }

    public String verify(LoginRequest users) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        users.getEmail(),
                        users.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(users.getEmail());
        }
        throw new RuntimeException("Invalid credentials");
    }
}
