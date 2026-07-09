package com.g9team10.backend;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveLogarComSucesso() throws Exception {
		String json = "{\"email\":\"teste@teste.com\", \"senha\":\"123456\"}";
		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk());
	}

	@Test
	void deveRetornar404SeUsuarioNaoExiste() throws Exception {
		String json = "{\"email\":\"naoexiste@teste.com\", \"senha\":\"123456\"}";
		mockMvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isNotFound());
	}

}
