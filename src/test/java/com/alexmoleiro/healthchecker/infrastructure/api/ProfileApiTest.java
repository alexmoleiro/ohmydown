package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static java.time.LocalDateTime.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileApiTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  HealthCheckRepository healthCheckRepository;

  @MockBean
  OauthService oauthService;

  @Autowired
  ProfileRepository profileRepository;

  @Test
  void shouldAddDomain() throws Exception {

    final User user = new User("endpoint", "alex@email.com");
    when(oauthService.getUser(anyString())).thenReturn(user);
    String aToken = "aToken";
    final String validUrl = "https://www.as.com";

    this.mockMvc.perform(
        post("/profile/addurl")
            .header("Token", aToken)
            .contentType(APPLICATION_JSON)
    .content("""
        {"url":"%s"}""".formatted(validUrl)))
        .andExpect(status().isCreated());

    assertThat(profileRepository.get(user).get().getFollowing()).usingRecursiveComparison()
        .isEqualTo(Set.of(new Endpoint(new HttpUrl("https://www.as.com"))));
    }

  @Test
  void shouldReturn404whenInvalidDomain() throws Exception {
    String aToken = "aToken";
    this.mockMvc.perform(
        post("/profile/addurl")
            .header("Token", aToken)
            .contentType(APPLICATION_JSON)
            .content("""
        {"url":"invalidUrl"}"""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnForbiddenWhenInvalidTokenTryingToAddAUrl() throws Exception {
    doThrow(new InvalidTokenException(new Exception()))
        .when(oauthService).getUser(anyString());

    this.mockMvc.perform(post("/profile/addurl")
        .header("Token", "")
        .contentType(APPLICATION_JSON)
        .content("""
        {"url":"https://www.as.com"}"""))
        .andExpect(status().isForbidden());
    }

  @Test
  void shouldReturnForbiddenWhenInvalidToken() throws Exception {
    doThrow(new InvalidTokenException(new Exception()))
        .when(oauthService).getUser(anyString());

    this.mockMvc.perform(get("/profile")
        .header("Token", ""))
        .andExpect(status().isForbidden());
    }

  @Test
  void shouldRespondFollowebWebsites() throws Exception {

    final String anId = randomString();
    final String anEmail = "alex@email2.com";
    final String aToken = randomString();
    LocalDateTime time = of(2020, 11, 30, 12, 0);
    final User aUser = new User(anId, anEmail);
    when(oauthService.getUser(aToken)).thenReturn(aUser);

    HttpUrl httpUrlA = new HttpUrl("https://amazon.com");
    Endpoint endpointA = new Endpoint(httpUrlA);
    HttpUrl httpUrlB = new HttpUrl("https://sport.it");
    Endpoint endPointB = new Endpoint(httpUrlB);
    HttpUrl httpUrlC = new HttpUrl("https://joindrover.com");
    Endpoint endpointC = new Endpoint(httpUrlC);

    profileRepository.addEndpoint(aUser, endpointA);
    profileRepository.addEndpoint(aUser, endPointB);
    profileRepository.addEndpoint(aUser, endpointC);

    healthCheckRepository.add(endpointA, new HealthCheckResponse(httpUrlA, 200, time.minusMinutes(1), time ));
    healthCheckRepository.add(endpointA, new HealthCheckResponse(httpUrlA, 200, time.minusMinutes(1), time ));

    healthCheckRepository.add(endPointB, new HealthCheckResponse(httpUrlB, 200, time.minusMinutes(1), time ));
    healthCheckRepository.add(endPointB, new HealthCheckResponse(httpUrlB, 200, time.minusMinutes(1), time ));

    healthCheckRepository.add(endpointC, new HealthCheckResponse(httpUrlC, 200, time.minusMinutes(1), time ));
    healthCheckRepository.add(endpointC, new HealthCheckResponse(httpUrlC, 200, time.minusMinutes(1), time ));

    this.mockMvc.perform(get("/profile").header("Token", aToken))
        .andExpect(status().isOk())
        .andExpect(content().json("""              
              {"responses":[
              {"endpoint":{"id":"%s","url":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"https://amazon.com","delay":60000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"https://amazon.com","delay":60000,"status":200}]}
              ,{"endpoint":{"id":"%s","url":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"https://sport.it","delay":60000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"https://sport.it","delay":60000,"status":200}]},
              {"endpoint":{"id":"%s","url":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"https://joindrover.com","delay":60000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"https://joindrover.com","delay":60000,"status":200}]}],
              "userId":"%s"}
              """.formatted(
                      endpointA.getId(), endpointA.getHttpUrl().toString(),
                      endPointB.getId(), endPointB.getHttpUrl().toString(),
                endpointC.getId(),  endpointC.getHttpUrl().toString(),
                anId
        )));
  }

  private String randomString() {
    return randomUUID().toString();
  }
}

