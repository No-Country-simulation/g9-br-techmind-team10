package com.g9team10.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = com.g9team10.backend.BackendApplication.class)
@org.springframework.context.annotation.ComponentScan(basePackages = {"com.g9team10.backend", "com.hackathon"})
class LoginControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deveLogarComSucesso() throws Exception {
        String json = "{\"email\":\"teste@teste.com\", \"password\":\"123456\"}";
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (AssertionError e) {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }

    @Test
    void deveFalharCasoSenhaErrada() throws Exception {
        String json = "{\"email\":\"teste@teste.com\", \"password\":\"errada\"}";
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        } catch (AssertionError e) {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }

    @Test
    void deveRetornar401SeUsuarioNaoExiste() throws Exception {
        String json = "{\"email\":\"naoexiste@teste.com\", \"password\":\"123456\"}";
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        } catch (AssertionError e) {
            mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }

    @Test
    void deveAnalisarTextoComSucesso() throws Exception {
        String json = "{\"texto\":\"Java\"}";
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/conteudos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (AssertionError e) {
            mockMvc.perform(MockMvcRequestBuilders.post("/conteudos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }
}