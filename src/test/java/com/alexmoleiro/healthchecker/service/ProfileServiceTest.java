package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.infrastructure.repositories.EndpointInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.ProfileRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class ProfileServiceTest {

  private static final User RANDOM_USER = new User("randomUser", "a@a.com");
  private static final User ANOTHER_RANDOM_USER = new User("randomUser2", "a@b.com");
  private static final Endpoint ENDPOINT = new Endpoint(new HttpUrl("www.as.com"));
  private static final Endpoint ENDPOINT_B = new Endpoint(new HttpUrl("www.b.com"));
  private static final int MAX_ENDPOINTS_PER_USER_LIMIT = 10;
  public static final int ONE_ENDPOINT_PER_USER = 1;

  @Test
  void shouldThrownExceptionWhenMaxNumberOFEndpointExeeded() {
    final ProfileService profileService =
        new ProfileService(
            new ProfileRepositoryInMemory(),
            new HealthChecksInMemory(),
            new EndpointInMemory(),
            mock(HealthChecker.class),
            ONE_ENDPOINT_PER_USER);

    profileService.addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT_B);

    assertThatThrownBy(
            () -> profileService.addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT))
        .isInstanceOf(MaximumEndpointPerUserExceededException.class);
  }

  @Test
  void shouldRespondEmptyHealthCheckResponses() {
    final ProfileRepositoryInMemory profileRepositoryInMemory = new ProfileRepositoryInMemory();
    final HealthCheckRepository healthCheckRepository = new HealthChecksInMemory();
    HealthChecker healthChecker = mock(HealthChecker.class);

    final List<HealthCheckResponses> healthCheckResponses =
        new ProfileService(
                profileRepositoryInMemory,
                healthCheckRepository,
                new EndpointInMemory(),
                healthChecker,
                MAX_ENDPOINTS_PER_USER_LIMIT)
            .getResponses(RANDOM_USER);

    assertThat(healthCheckResponses).isEqualTo(emptyList());
  }

  @Test
  void shouldAddEndpointToProfileAndEndpointRepository() {

    EndpointRepository endpointRepository = mock(EndpointRepository.class);
    ProfileRepository profileRepository = mock(ProfileRepository.class);
    HealthChecker healthChecker = mock(HealthChecker.class);
    HealthCheckRepository healthCheckRepository = mock(HealthCheckRepository.class);

    final HealthCheckResponse healthCheckResponse =
        new HealthCheckResponse(ENDPOINT.getHttpUrl(), OK.value(), now(), now());

    when(healthChecker.check(ENDPOINT.getHttpUrl())).thenReturn(healthCheckResponse);

    new ProfileService(
            profileRepository,
            healthCheckRepository,
            endpointRepository,
            healthChecker,
            MAX_ENDPOINTS_PER_USER_LIMIT)
        .addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT);

    verify(endpointRepository).add(ENDPOINT);
    verify(profileRepository).addEndpoint(RANDOM_USER, ENDPOINT);
    verify(healthCheckRepository).add(ENDPOINT, healthCheckResponse);
  }

  @Test
  void shouldAddExistingEndpointToAnotherUser() {

    ProfileRepository profileRepository = mock(ProfileRepository.class);
    HealthChecker healthChecker = mock(HealthChecker.class);

    ProfileService profileService =
        new ProfileService(
            profileRepository,
            new HealthChecksInMemory(),
            new EndpointInMemory(),
            healthChecker,
            MAX_ENDPOINTS_PER_USER_LIMIT);

    profileService.addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT);
    profileService.addEndpointToEndpointsAndUserProfile(ANOTHER_RANDOM_USER, ENDPOINT);

    verify(profileRepository).addEndpoint(RANDOM_USER, ENDPOINT);
    verify(profileRepository).addEndpoint(ANOTHER_RANDOM_USER, ENDPOINT);
  }

  @Test
  void shouldDeleteAllTheEndpoints() {
    ProfileRepository profileRepository = mock(ProfileRepository.class);
    HealthChecker healthChecker = mock(HealthChecker.class);
    EndpointInMemory endpointRepository = new EndpointInMemory();
    endpointRepository.add(ENDPOINT);
    endpointRepository.add(ENDPOINT_B);
    var profileService =
        new ProfileService(
            profileRepository,
            new HealthChecksInMemory(),
            endpointRepository,
            healthChecker,
            MAX_ENDPOINTS_PER_USER_LIMIT);

    profileService.deleteUrls(RANDOM_USER, of(ENDPOINT.getId(), ENDPOINT_B.getId()));

    verify(profileRepository).deleteEndpoint(RANDOM_USER, ENDPOINT_B);
    verify(profileRepository).deleteEndpoint(RANDOM_USER, ENDPOINT);
  }
}
