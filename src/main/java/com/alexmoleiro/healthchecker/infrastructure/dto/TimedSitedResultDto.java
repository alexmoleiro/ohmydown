package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponses;

public class TimedSitedResultDto {
  private final TimedHealthCheckResponses timedHealthCheckResponses;
  private HealthCheckResponse newestHealthCheckResponse;

  public TimedSitedResultDto(TimedHealthCheckResponses timedHealthCheckResponses) {
    this.timedHealthCheckResponses = timedHealthCheckResponses;
    this.newestHealthCheckResponse = timedHealthCheckResponses.getHealthCheckResponse().get(0);
  }

  public String getId() {
    return timedHealthCheckResponses.getId().getValue();
  }

  public int getStatus() {
    return newestHealthCheckResponse.getStatus();
  }

  public String getUrl() {
    return newestHealthCheckResponse.getUrl().toString();
  }

  public long getDelay() {
    return newestHealthCheckResponse.getDelay();
  }
}
