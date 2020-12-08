package com.alexmoleiro.healthchecker.core;

import java.time.LocalDateTime;

public class TimedHealthCheckResponse {

  private final Id id;
  private final LocalDateTime localDateTime;
  private final HealthCheckResponse healthCheckResponse;

  public TimedHealthCheckResponse(Id id, LocalDateTime time, HealthCheckResponse response) {
    this.id = id;
    this.localDateTime = time;
    this.healthCheckResponse = response;
  }

  public Id getId() {
    return id;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public HealthCheckResponse getHealthCheckResponse() {
    return healthCheckResponse;
  }
}
