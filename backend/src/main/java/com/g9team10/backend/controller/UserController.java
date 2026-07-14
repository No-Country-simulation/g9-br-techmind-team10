package com.g9team10.backend.controller;

import com.g9team10.backend.dto.AuthResponse;
import com.g9team10.backend.dto.LoginRequest;
import com.g9team10.backend.dto.RegisterRequest;
import com.g9team10.backend.dto.RegisterResponse;
import com.g9team10.backend.model.User;
import com.g9team10.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @RequestBody @Valid RegisterRequest request) {

        User user = service.register(request);
        return new RegisterResponse(user.getName(), user.getEmail());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request){

        return ResponseEntity.ok(service.login(request));


    }

}
