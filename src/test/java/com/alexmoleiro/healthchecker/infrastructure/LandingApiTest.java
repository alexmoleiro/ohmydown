package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static java.time.Duration.ofMillis;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LandingApiTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  HealthCheckResultsRepository repository;

  @Test
  void shouldReturnLandingListSites() throws Exception {
    repository.add(new HealthCheckResponse(new URL("https://www.alexmoleiro.com"), OK.value(),
        ofMillis(200)));
    repository.add(new HealthCheckResponse(new URL("https://www.yavendras.com"), INTERNAL_SERVER_ERROR.value(),
        ofMillis(123)
    ));

      this.mockMvc.perform(post("/landing-list"))
          .andExpect(status().isOk())
          .andExpect(content().json("""
              {"responses":[
              {"url":"https://www.alexmoleiro.com","delay":200,"status":200},
              {"url":"https://www.yavendras.com","delay":123,"status":500}
              ],
              "numUrls":2}"""));
  }

}