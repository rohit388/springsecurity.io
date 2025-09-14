package com.springsecurity.io.service.imple;

import com.springsecurity.io.config.JwtService;
import com.springsecurity.io.dto.LoginRequest;
import com.springsecurity.io.dto.OtpVerificationRequest;
import com.springsecurity.io.dto.PasswordlessLoginRequest;
import com.springsecurity.io.dto.UserRequest;
import com.springsecurity.io.entity.Role;
import com.springsecurity.io.entity.Users;
import com.springsecurity.io.exception.InvalidOtpException;
import com.springsecurity.io.exception.ResourceNotFoundException;
import com.springsecurity.io.mapper.UserMapper;
import com.springsecurity.io.repo.RoleRepository;
import com.springsecurity.io.repo.UserRepository;
import com.springsecurity.io.service.AuthService;
import com.springsecurity.io.service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImple implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public AuthServiceImple(AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
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

    @Override
    public void requestOtp(PasswordlessLoginRequest passwordlessLoginRequest) {
        Users user = userRepository.findByEmail(passwordlessLoginRequest.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + passwordlessLoginRequest.getEmail());
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10)); // OTP valid for 10 minutes
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    public String verifyOtpAndLogin(OtpVerificationRequest otpVerificationRequest) {
        Users user = userRepository.findByEmail(otpVerificationRequest.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + otpVerificationRequest.getEmail());
        }

        if (user.getOtp() == null || !user.getOtp().equals(otpVerificationRequest.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        if (user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired");
        }

        // Clear OTP after successful verification
        user.setOtp(null);
        user.setOtpExpiryTime(null);
        userRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }

    private String generateOtp() {
        // Generate a 6-digit OTP
        return String.format("%06d", new Random().nextInt(999999));
    }
}
