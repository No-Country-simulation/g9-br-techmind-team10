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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @DisplayName("Deve processar login com credenciais válidas")
    void deveProcessarLoginComSucesso() {
        // Arrange: 👇 AQUI ESTAVA FALTANDO — definimos o comportamento do mock
        String emailValido = "teste@email.com";
        String senhaValida = "senha123";
        
        LoginRequest requisicao = new LoginRequest(emailValido, senhaValida);
        
        // Simulamos um usuário existente no banco (sem precisar do Oracle!)
        User usuarioMock = new User();
        usuarioMock.setEmail(emailValido);
        usuarioMock.setPassword("senhaCodificada");

        // Ensina o mock a retornar o usuário quando buscar pelo email
        when(userRepository.findByEmail(eq(emailValido))).thenReturn(Optional.of(usuarioMock));
        // Ensina o mock a confirmar que a senha confere
        when(passwordEncoder.matches(eq(senhaValida), eq(usuarioMock.getPassword()))).thenReturn(true);
        // Ensina o mock a retornar um token qualquer
        when(jwtService.gerarToken(any(User.class))).thenReturn("token-falso-para-teste");

        // Act: executa o método REAL do serviço
        String tokenGerado = userService.login(requisicao);

        // Assert: valida todo o fluxo
        assertNotNull(requisicao, "Requisição não pode ser nula");
        assertNotNull(tokenGerado, "Deve retornar um token válido");
        verify(userRepository).findByEmail(eq(emailValido));
        verify(passwordEncoder).matches(eq(senhaValida), eq(usuarioMock.getPassword()));
        verify(jwtService).gerarToken(any(User.class));
    }
}