package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.infrastructure.repositories.EndpointInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.ProfileRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProfileServiceTest {

  public static final User RANDOM_USER = new User("randomUser", "a@a.com");
  public static final User ANOTHER_RANDOM_USER = new User("randomUser2", "a@b.com");
  public static final Endpoint ENDPOINT = new Endpoint(new HttpUrl("www.as.com"));

  @Test
  void shouldRespondEmptyHealthCheckResponses() {
    final ProfileRepositoryInMemory profileRepositoryInMemory = new ProfileRepositoryInMemory();
    final HealthCheckRepository healthCheckRepository = new HealthChecksInMemory();

    final List<HealthCheckResponses> healthCheckResponses = new ProfileService(
            profileRepositoryInMemory,
            healthCheckRepository,
            new EndpointInMemory())
        .getResponses(RANDOM_USER);

    assertThat(healthCheckResponses).isEqualTo(emptyList());
  }

  @Test
  void shouldAddEndpointToProfileAndEndpointRepository() {


    EndpointRepository endpointRepository = mock(EndpointRepository.class);
    ProfileRepository profileRepository = mock(ProfileRepository.class);


    new ProfileService(profileRepository, new HealthChecksInMemory(), endpointRepository)
            .addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT);

    verify(endpointRepository).add(ENDPOINT);
    verify(profileRepository).addEndpoint(RANDOM_USER, ENDPOINT);

  }

  @Test
  void shouldAddExistingEndpointToAnotherUser() {

    ProfileRepository profileRepository = mock(ProfileRepository.class);

    ProfileService profileService = new ProfileService(
            profileRepository,
            new HealthChecksInMemory(),
            new EndpointInMemory());

    profileService.addEndpointToEndpointsAndUserProfile(RANDOM_USER, ENDPOINT);
    profileService.addEndpointToEndpointsAndUserProfile(ANOTHER_RANDOM_USER, ENDPOINT);

    verify(profileRepository).addEndpoint(RANDOM_USER, ENDPOINT);
    verify(profileRepository).addEndpoint(ANOTHER_RANDOM_USER, ENDPOINT);

  }
}