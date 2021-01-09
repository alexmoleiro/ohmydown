package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;

public class TimedSitedResultDto {
  private final HealthCheckResponses healthCheckResponses;
  private HealthCheckResponse lastResponse;
  private final float uptime;
  private final double average;

  public TimedSitedResultDto(HealthCheckResponses healthCheckResponses, float uptime, double average) {
    this.healthCheckResponses = healthCheckResponses;
    this.lastResponse = healthCheckResponses.getHealthCheckResponse().getLast();
    this.uptime = uptime;
    this.average = average;
  }

  public String getId() {
    return healthCheckResponses.getEndpoint().getId();
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

  public String getGroup() {
    return healthCheckResponses.getEndpoint().getGroup();
  }

  public double getAverage() {
    return  average;
  }

  public float getUptime() {
    return  uptime;
  }
}
