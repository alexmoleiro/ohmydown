package com.alexmoleiro.healthchecker.core;

import java.net.URL;
import java.time.LocalDateTime;

public class TimedHealthCheckResponse {

  private final Id id;
  private final LocalDateTime localDateTime;
  private final HealthCheckResponse healthCheckResponse;

  public TimedHealthCheckResponse(Id id, LocalDateTime localDateTime, HealthCheckResponse healthCheckResponse) {
    this.id = id;
    this.localDateTime = localDateTime;
    this.healthCheckResponse = healthCheckResponse;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public Id getId() {
    return id;
  }

  public HealthCheckResponse getHealthCheckResponse() {
    return healthCheckResponse;
  }

  public URL getUrl() {
    return healthCheckResponse.getUrl();
  }
}
