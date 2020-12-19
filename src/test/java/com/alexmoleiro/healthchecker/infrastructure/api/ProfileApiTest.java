package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileApiTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  OauthService oauthService;

  @Test
  void shouldResolveToken() throws Exception {

    final String anId = "id";
    final String anEmail = "alex@email.com";
    final String aToken = "anything";
    when(oauthService.getUser(aToken)).thenReturn(new User(anId, anEmail));

    this.mockMvc.perform(get("/profile").header("Token", aToken))
        .andExpect(status().isOk())
        .andExpect(content().json("""              
              {"id":"%s","email":"%s"}
              """.formatted(anId, anEmail)));
  }
}