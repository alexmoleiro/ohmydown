package com.alexmoleiro.healthchecker.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingApiTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void shouldReturnLandingListSites() throws Exception {
      this.mockMvc.perform(post("/landing-list"))
          .andExpect(status().isOk())
          .andExpect(content().json("""
              {"siteResults":[{"url":"https://www.yavendras.com","delay":200,"siteStatus":"UP"},
              {"url":"https://www.alexmoleiro.com","delay":123,"siteStatus":"DOWN"}],
              "numUrls":2}"""));

  }
}
