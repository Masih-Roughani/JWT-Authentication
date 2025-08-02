package com.example.demo.service;

import com.example.demo.model.JwtUtil;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.repository.UserRepository;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class AuthService {
    private static final String FIXED_SALT = "$2a$10$abcdefghijklmNOPQRSTUV1234567890123456";
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public void addUser(UserDTO userDTO) {
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), FIXED_SALT);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(hashedPassword);
        user.setRole(Role.User);
        userRepository.save(user);
    }

    public String authenticate(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        String pass = BCrypt.hashpw(userDTO.getPassword(), FIXED_SALT);
        if (user != null && user.getPassword().equals(pass)) {
            return jwtUtil.generateToken(user.getUsername());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
