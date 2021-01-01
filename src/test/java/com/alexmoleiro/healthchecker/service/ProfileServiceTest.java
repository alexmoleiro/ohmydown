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

    Endpoint endpoint = new Endpoint(new HttpUrl("www.as.com"));

    new ProfileService(profileRepository, new HealthChecksInMemory(), endpointRepository)
            .addEndpoint(RANDOM_USER, endpoint);

    verify(endpointRepository).add(endpoint);
    verify(profileRepository).addEndpoint(RANDOM_USER, endpoint);

  }
}