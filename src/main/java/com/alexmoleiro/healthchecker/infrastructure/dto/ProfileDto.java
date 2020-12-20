package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.util.List;

import static java.util.List.of;

public class ProfileDto {

  private final User user;
  private final HealthCheckRepository healthCheckRepository;

  public ProfileDto(User user, HealthCheckRepository healthCheckRepository) {
    this.user = user;
    this.healthCheckRepository = healthCheckRepository;
  }

  public String getUserId() {
    return user.getId();
  }

  public List<HealthCheckResponses> getResponses() {
    final List<Id> ids = of(new Id("amazon.com"), new Id("sport.it"), new Id("joindrover.com"));
    return healthCheckRepository.getResponses(ids);
  }
}
