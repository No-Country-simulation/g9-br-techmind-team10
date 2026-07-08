package com.g9team10.backend.controller;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conteudos")
public class ConteudoController {

    @PostMapping
    public Map<String, Object> analisarTexto(@RequestBody Map<String, String> payload) {
        String texto = payload.get("texto");

        // MOCK: Por enquanto responde fixo. Depois chamamos o Python.
        return Map.of(
                "categoria", "Backend",
                "probabilidade", 0.89,
                "informacoes_adicionais", List.of("Java", "Spring Boot", "API REST")
        );
    }
}
