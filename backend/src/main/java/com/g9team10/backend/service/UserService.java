package com.g9team10.backend.service;

import com.g9team10.backend.dto.LoginRequest;
import com.g9team10.backend.dto.RegisterRequest;
import com.g9team10.backend.model.User;
import com.g9team10.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void register(RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    public boolean login(LoginRequest dto) {

        Optional<User> user = userRepository.findByEmail(dto.email());

        if (user.isEmpty()) {
            return false;
        }

        return passwordEncoder.matches(
                dto.password(),
                user.get().getPassword()
        );
    }



}
