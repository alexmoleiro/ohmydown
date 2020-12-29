package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class HealthCheckResultsApiTest {

  public static final String URL_STRING = "https://www.a.com";
  @Autowired
  MockMvc mockMvc;

  @Autowired
  HealthCheckRepository repository;

  @Test
  void shouldReturnHistoricValues() throws Exception {

    final LocalDateTime first = of(2020, 12, 8, 23, 20);
    final LocalDateTime second = of(2020, 12, 8, 23, 25);
    repository.deleteAll();
    repository.add(
        new Endpoint(new HttpUrl("www.a.com")),
        new HealthCheckResponse(new HttpUrl(URL_STRING), OK.value(), first.minusHours(1), first)
    );

    repository.add(
        new Endpoint(new HttpUrl("www.a.com")),
        new HealthCheckResponse(new HttpUrl(URL_STRING), INTERNAL_SERVER_ERROR.value(), second.minusHours(1), second)
    );

    this.mockMvc.perform(get("/historical/www.a.com"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
              [
              {"url":"https://www.a.com","delay":3600000,"status":200,"time":"2020-12-08T23:20"},
              {"url":"https://www.a.com","delay":3600000,"status":500,"time":"2020-12-08T23:25"}
              ]"""));
    }

    //TODO flaky tests
  @Test
  void shouldReturnLandingListSites() throws Exception {
    LocalDateTime now = now();
    repository.deleteAll();
    repository.add(
        new Endpoint(new HttpUrl("https://www.z.com"))
            , new HealthCheckResponse(new HttpUrl("https://www.z.com"), OK.value(), now, now));

    repository.add(new Endpoint(new HttpUrl("https://www.x.com")),
        new HealthCheckResponse(new HttpUrl("https://www.x.com"), INTERNAL_SERVER_ERROR.value(), now, now
    ));

      this.mockMvc.perform(post("/landing-list"))
          .andExpect(status().isOk())
          .andExpect(content().json("""
              {"responses":[
              {"url":"https://www.z.com","delay":0,"status":200,"id":"https://www.z.com"},
              {"url":"https://www.x.com","delay":0,"status":500,"id":"https://www.x.com"}
              ],
              "numUrls":2}"""));
  }
}