package com.g9team10.backend;

import com.g9team10.backend.api.dto.request.LoginRequest;
import com.g9team10.backend.domain.model.User;
import com.g9team10.backend.domain.service.UserService;
import com.g9team10.backend.domain.repository.UserRepository;
import com.g9team10.backend.core.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - Fluxo de Login (UserService)")
class LoginControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve gerar token com credenciais corretas")
    void deveLogarComSucesso() {
        String email = "teste@email.com";
        String senha = "senha123";
        LoginRequest req = new LoginRequest(email, senha);
        User usuario = new User();
        usuario.setEmail(email);
        usuario.setPassword("hash-correta");

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(eq(senha), eq(usuario.getPassword()))).thenReturn(true);
        when(jwtService.gerarToken(any(User.class))).thenReturn("token-valido");

        String token = userService.login(req);

        assertAll(
            () -> assertNotNull(token, "Token não pode ser nulo"),
            () -> assertFalse(token.isBlank(), "Token não pode ser vazio"),
            () -> verify(userRepository).findByEmail(eq(email)),
            () -> verify(passwordEncoder).matches(eq(senha), eq(usuario.getPassword())),
            () -> verify(jwtService).gerarToken(any(User.class))
        );
    }

    @Test
    @DisplayName("Deve lançar erro quando e-mail não for cadastrado")
    void erroQuandoEmailNaoExiste() {
        LoginRequest req = new LoginRequest("naoexiste@email.com", "qualquer");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(req));
    }

    @Test
    @DisplayName("Deve lançar erro quando senha estiver incorreta")
    void erroQuandoSenhaInvalida() {
        String email = "teste@email.com";
        LoginRequest req = new LoginRequest(email, "senha-errada");
        User usuario = new User();
        usuario.setEmail(email);
        usuario.setPassword("hash-correta");

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(eq("senha-errada"), eq(usuario.getPassword()))).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(req));
    }
}