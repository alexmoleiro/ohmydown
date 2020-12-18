package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.ProfileUser;
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
class ProfileUserTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ProfileUser profileUser;

  @Test
  void shouldResolveToken() throws Exception {

    final String aName = "Alejandro";
    final String aToken = "anything";
    when(profileUser.getName(aToken)).thenReturn(aName);

    this.mockMvc.perform(get("/profile").header("token", aToken))
        .andExpect(status().isOk())
        .andExpect(content().json("""              
              {"name":"%s"}
              """.formatted(aName)));
  }
}