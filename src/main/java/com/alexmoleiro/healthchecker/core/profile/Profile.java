package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;

import java.util.List;

import static java.util.List.of;

public class Profile {

  private final User user;
  private final HealthCheckResultsRepository healthCheckResultsRepository;

  public Profile(User user, HealthCheckResultsRepository healthCheckResultsRepository) {
    this.user = user;
    this.healthCheckResultsRepository = healthCheckResultsRepository;
  }

  public String getUserId() {
    return user.getId();
  }

  public List<HealthCheckResponses> getResponses() {
    final List<Id> ids = of(new Id("amazon.com"), new Id("sport.it"), new Id("joindrover.com"));
    return healthCheckResultsRepository.getResponses(ids);
  }
}
