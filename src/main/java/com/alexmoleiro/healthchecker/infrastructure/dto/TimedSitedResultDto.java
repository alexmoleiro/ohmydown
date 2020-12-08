package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;

public class TimedSitedResultDto {
  private final TimedHealthCheckResponses timedHealthCheckResponses;
  private HealthCheckResponse lastResponse;

  public TimedSitedResultDto(TimedHealthCheckResponses timedHealthCheckResponses) {
    this.timedHealthCheckResponses = timedHealthCheckResponses;
    this.lastResponse = timedHealthCheckResponses.getHealthCheckResponse().getLast();
  }

  public String getId() {
    return timedHealthCheckResponses.getId().getValue();
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
