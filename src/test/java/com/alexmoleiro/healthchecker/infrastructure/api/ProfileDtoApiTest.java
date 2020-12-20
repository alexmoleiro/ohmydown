package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileDtoApiTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  HealthCheckRepository healthCheckRepository;

  @MockBean
  OauthService oauthService;

  @Test
  void shouldResolveToken() throws Exception {

    final String anId = "id";
    final String anEmail = "alex@email.com";
    final String aToken = "anything";
    LocalDateTime time = LocalDateTime.of(2020, 11, 30, 12, 00);
    when(oauthService.getUser(aToken)).thenReturn(new User(anId, anEmail));

    healthCheckRepository.add(new Id("amazon.com"), new HealthCheckResponse(new URL("https://amazon.com"), 200,
        time.minusMinutes(1), time ));
    healthCheckRepository.add(new Id("amazon.com"), new HealthCheckResponse(new URL("https://amazon.com"), 200,
        time.minusMinutes(1), time ));

    healthCheckRepository.add(new Id("sport.it"), new HealthCheckResponse(new URL("https://sport.it"), 200,
        time.minusMinutes(1), time ));
    healthCheckRepository.add(new Id("sport.it"), new HealthCheckResponse(new URL("https://sport.it"), 200,
        time.minusMinutes(1), time ));

    healthCheckRepository.add(new Id("joindrover.com"), new HealthCheckResponse(new URL("https://joindrover.com"), 200,
        time.minusMinutes(1), time ));
    healthCheckRepository.add(new Id("joindrover.com"), new HealthCheckResponse(new URL("https://joindrover.com"), 200,
        time.minusMinutes(1), time ));

    this.mockMvc.perform(get("/profile").header("Token", aToken))
        .andExpect(status().isOk())
        .andExpect(content().json("""              
              {"responses":[{"id":{"value":"amazon.com"},"healthCheckResponse":[{"time":"2020-11-30T12:00:00","url":"https://amazon.com","delay":60000,"status":200},{"time":"2020-11-30T12:00:00","url":"https://amazon.com","delay":60000,"status":200}]},{"id":{"value":"sport.it"},"healthCheckResponse":[{"time":"2020-11-30T12:00:00","url":"https://sport.it","delay":60000,"status":200},{"time":"2020-11-30T12:00:00","url":"https://sport.it","delay":60000,"status":200}]},{"id":{"value":"joindrover.com"},"healthCheckResponse":[{"time":"2020-11-30T12:00:00","url":"https://joindrover.com","delay":60000,"status":200},{"time":"2020-11-30T12:00:00","url":"https://joindrover.com","delay":60000,"status":200}]}],"userId":"id"}
              """));
  }
}