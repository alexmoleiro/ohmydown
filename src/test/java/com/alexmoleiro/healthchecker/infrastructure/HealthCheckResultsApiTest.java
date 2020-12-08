package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.Id;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;

import static java.time.Duration.ofMillis;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HealthCheckResultsApiTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  HealthCheckResultsRepository repository;

  @Test
  void shouldReturnHistoricValues() throws Exception {
    repository.add(new TimedHealthCheckResponses(
        new Id("www.a.com"), now(),
        new HealthCheckResponse(new URL("https://www.a.com"), OK.value(), ofMillis(444))));

    repository.add(new TimedHealthCheckResponses(
        new Id("www.a.com"), now(),
        new HealthCheckResponse(new URL("https://www.a.com"), INTERNAL_SERVER_ERROR.value(), ofMillis(123)
        )));

    this.mockMvc.perform(get("/historic"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
              [
              {"url":"https://www.a.com","delay":444,"status":200,"time":"20201201-125959"},
              {"url":"https://www.a.com","delay":123,"status":500,"time":"20201201-135958"}
              ]"""));

    }


  @Test
  void shouldReturnLandingListSites() throws Exception {
    repository.add(new TimedHealthCheckResponses(
        new Id("www.a.com"), now(), new HealthCheckResponse(new URL("https://www.a.com"), OK.value(),
        ofMillis(200))));
    repository.add(new TimedHealthCheckResponses(new Id("www.b.com"), now(),
        new HealthCheckResponse(new URL("https://www.b.com"), INTERNAL_SERVER_ERROR.value(),
        ofMillis(123)
    )));

      this.mockMvc.perform(post("/landing-list"))
          .andExpect(status().isOk())
          .andExpect(content().json("""
              {"responses":[
              {"url":"https://www.a.com","delay":200,"status":200,"id":"www.a.com"},
              {"url":"https://www.b.com","delay":123,"status":500,"id":"www.b.com"}
              ],
              "numUrls":2}"""));
  }

}