package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;

public class TimedSitedResultDto {
  private final TimedHealthCheckResponse timedHealthCheckResponse;

  public TimedSitedResultDto(TimedHealthCheckResponse timedHealthCheckResponse) {
    this.timedHealthCheckResponse = timedHealthCheckResponse;
  }

  public String getId() {
    return timedHealthCheckResponse.getId().getValue();
  }

  public int getStatus() {
    return timedHealthCheckResponse.getHealthCheckResponse().getStatus();
  }

  public String getUrl() {
    return timedHealthCheckResponse.getHealthCheckResponse().getUrl().toString();
  }

  public long getDelay() {
    return timedHealthCheckResponse.getHealthCheckResponse().getDelay();
  }
}
