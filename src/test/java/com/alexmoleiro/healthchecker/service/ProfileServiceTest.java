package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.ProfileRepositoryInMemory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class ProfileServiceTest {

  @Test
  void shouldRespondEmptyHealthCheckResponses() {
    final ProfileRepositoryInMemory profileRepositoryInMemory = new ProfileRepositoryInMemory();
    final HealthCheckRepository healthCheckRepository = new HealthChecksInMemory();

    final List<HealthCheckResponses> healthCheckResponses = new ProfileService(profileRepositoryInMemory, healthCheckRepository)
        .getResponses(new User("randomUser", "a@a.com"));

    assertThat(healthCheckResponses).isEqualTo(emptyList());
  }

}