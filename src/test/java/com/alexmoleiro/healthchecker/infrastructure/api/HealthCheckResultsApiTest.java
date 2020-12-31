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
  private HealthCheckRepository repository;
  private static final LocalDateTime FIRST = of(2020, 12, 8, 23, 20);
  private static final LocalDateTime SECOND = of(2020, 12, 8, 23, 25);

  @Test
  void shouldReturnHistoricValues() throws Exception {

    repository.deleteAll();
    repository.add(
        new Endpoint(new HttpUrl("www.a.com")),
        new HealthCheckResponse(new HttpUrl(URL_STRING), OK.value(), FIRST.minusHours(1), FIRST)
    );

    repository.add(
        new Endpoint(new HttpUrl("www.a.com")),
        new HealthCheckResponse(new HttpUrl(URL_STRING), INTERNAL_SERVER_ERROR.value(), SECOND.minusHours(1), SECOND)
    );

    this.mockMvc.perform(get("/historical/www.a.com"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
              [
              {"url":"https://www.a.com","delay":3600000,"status":200,"time":"2020-12-08T23:20"},
              {"url":"https://www.a.com","delay":3600000,"status":500,"time":"2020-12-08T23:25"}
              ]"""));
    }


  @Test
  void shouldReturnLandingListSites() throws Exception {
    repository.deleteAll();
    HttpUrl httpUrlZ = new HttpUrl("https://www.z.com");
    Endpoint endpointZ = new Endpoint(httpUrlZ);
    repository.add(endpointZ, new HealthCheckResponse(httpUrlZ, OK.value(), FIRST, SECOND));

    HttpUrl httpUrlX = new HttpUrl("https://www.x.com");
    Endpoint endpointX = new Endpoint(httpUrlX);
    repository.add(endpointX, new HealthCheckResponse(httpUrlX, INTERNAL_SERVER_ERROR.value(), FIRST, SECOND
    ));

      this.mockMvc.perform(post("/landing-list"))
          .andExpect(status().isOk())
          .andExpect(content().json("""
              {"responses":[
              {"url":"https://www.z.com","delay":300000,"status":200,"id":"%s"},
              {"url":"https://www.x.com","delay":300000,"status":500,"id":"%s"}
              ],
              "numUrls":2}""".formatted(endpointZ.getId(), endpointX.getId())));
  }
}