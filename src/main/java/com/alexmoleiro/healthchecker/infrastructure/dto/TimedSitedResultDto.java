package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

public class TimedSitedResultDto {
  private final HealthCheckResponses healthCheckResponses;
  private HealthCheckResponse lastResponse;

  public TimedSitedResultDto(HealthCheckResponses healthCheckResponses) {
    this.healthCheckResponses = healthCheckResponses;
    this.lastResponse = healthCheckResponses.getHealthCheckResponse().getLast();
  }

  public String getId() {
    return healthCheckResponses.getId().getValue();
  }

  public int getStatus() {
    return lastResponse.getStatus();
  }

  public String getUrl() {
    return lastResponse.getUrl().toString();
  }

  public long getDelay() {
    return lastResponse.getDelay();
  }
}
