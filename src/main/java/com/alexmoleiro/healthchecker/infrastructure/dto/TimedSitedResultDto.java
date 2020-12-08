package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.TimedHealthCheckResponse;

public class TimedSitedResultDto {
  private final TimedHealthCheckResponse timedHealthCheckResponse;
  private HealthCheckResponse newestHealthCheckResponse;

  public TimedSitedResultDto(TimedHealthCheckResponse timedHealthCheckResponse) {
    this.timedHealthCheckResponse = timedHealthCheckResponse;
    this.newestHealthCheckResponse = timedHealthCheckResponse.getHealthCheckResponse().get(0);
  }

  public String getId() {
    return timedHealthCheckResponse.getId().getValue();
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
