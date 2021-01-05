package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.time.LocalDateTime.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
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

  @Autowired
  EndpointRepository endpointRepository;

  @MockBean
  OauthService oauthService;

  @Autowired
  ProfileRepository profileRepository;

  private static final String A_TOKEN = UUID.randomUUID().toString();
  private static final User USER = new User("1", "alex@email.com");
  private static final User USERB = new User("2", "alex@email.com");

  @Test
  void shouldAddDomain() throws Exception {

    when(oauthService.getUser(anyString())).thenReturn(USER);
    String aToken = "aToken";
    final String validUrl = "https://www.as.com";

    this.mockMvc.perform(
        post("/profile/addurl")
            .header("Token", aToken)
            .contentType(APPLICATION_JSON)
    .content("""
        {"url":"%s"}""".formatted(validUrl)))
        .andExpect(status().isCreated());

    assertThat(profileRepository.get(USER).get().getFollowing()).usingRecursiveComparison()
        .isEqualTo(Set.of(new Endpoint(new HttpUrl("https://www.as.com"))));
    }

  @Test
  void shouldReturn404whenInvalidDomain() throws Exception {
    this.mockMvc.perform(
        post("/profile/addurl")
            .header("Token", A_TOKEN)
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
  void deleteUrl() throws Exception {
    when(oauthService.getUser(anyString())).thenReturn(USERB);
    Endpoint endpointA = new Endpoint(new HttpUrl("a.com"));

    profileRepository.addEndpoint(USERB, endpointA);
    endpointRepository.add(endpointA);

    assertThat(profileRepository.get(USERB).get().getFollowing()).containsOnly(endpointA);

    this.mockMvc.perform(
            MockMvcRequestBuilders.delete("/profile/deleteurls")
                    .header("Token", A_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content("""
        {"ids":["%s"]}""".formatted(endpointA.getId())))
            .andExpect(status().isOk());

    assertThat(profileRepository.get(USERB).get().getFollowing()).isEmpty();

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
    final String anEmail = randomString();
    final String aToken = randomString();
    LocalDateTime time = of(2020, 11, 30, 12, 0);
    final User aUser = new User(anId, anEmail);

    when(oauthService.getUser(aToken)).thenReturn(aUser);

    Endpoint endpointA = new Endpoint(new HttpUrl("a.com"));
    Endpoint endPointB = new Endpoint(new HttpUrl("b.it"));
    Endpoint endpointC = new Endpoint(new HttpUrl("c.es"));

    List.of(endpointA, endPointB, endpointC).forEach(e->
    {
      profileRepository.addEndpoint(aUser,e);
      healthCheckRepository.add(e, new HealthCheckResponse(e.getHttpUrl(), OK.value(), time.minusMinutes(2), time ));
      healthCheckRepository.add(e, new HealthCheckResponse(e.getHttpUrl(), OK.value(), time.minusMinutes(1), time ));
    });


    this.mockMvc.perform(get("/profile").header("Token", aToken))
        .andExpect(status().isOk())
        .andExpect(content().json("""              
              {"responses":[
              {"endpoint":{"id":"%s","url":"%s","group":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"http://a.com","delay":120000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"http://a.com","delay":60000,"status":200}]}
              ,{"endpoint":{"id":"%s","url":"%s","group":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"http://b.it","delay":120000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"http://b.it","delay":60000,"status":200}]},
              {"endpoint":{"id":"%s","url":"%s","group":"%s"},
              "healthCheckResponse":[
              {"time":"2020-11-30T12:00:00","url":"http://c.es","delay":120000,"status":200},
              {"time":"2020-11-30T12:00:00","url":"http://c.es","delay":60000,"status":200}]}],
              "userId":"%s"}
              """.formatted(
                      endpointA.getId(), endpointA.getHttpUrl().toString(), endpointA.getGroup(),
                      endPointB.getId(), endPointB.getHttpUrl().toString(), endPointB.getGroup(),
                endpointC.getId(),  endpointC.getHttpUrl().toString(), endpointC.getGroup(),
                anId
        )));
  }

  private String randomString() {
    return randomUUID().toString();
  }
}

