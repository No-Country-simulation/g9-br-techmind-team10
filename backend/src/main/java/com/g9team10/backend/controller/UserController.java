package com.g9team10.backend.controller;


import com.g9team10.backend.dto.LoginRequest;
import com.g9team10.backend.dto.RegisterRequest;
import com.g9team10.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Valid RegisterRequest request) {

        service.register(request);

        return ResponseEntity.ok("Usuário cadastrado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request){

        boolean authenticated = service.login(request);

        if(authenticated) {
            return ResponseEntity.ok("Login realizado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
    }
}
