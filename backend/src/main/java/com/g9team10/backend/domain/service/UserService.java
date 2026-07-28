package com.g9team10.backend.domain.service;

import com.g9team10.backend.api.dto.request.LoginRequest;
import com.g9team10.backend.api.dto.request.RegisterRequest;
import com.g9team10.backend.api.dto.response.AuthResponse;
import com.g9team10.backend.core.security.JwtService;
import com.g9team10.backend.domain.exception.EmailAlwaysExistsException;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlwaysExistsException();
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }


    public AuthResponse login(LoginRequest dto) {

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        String token = jwtService.gerarToken(user);

        return new AuthResponse(token);

    }
}
