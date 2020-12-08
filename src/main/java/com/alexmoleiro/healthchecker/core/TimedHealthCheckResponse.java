package com.alexmoleiro.healthchecker.core;

import java.net.URL;
import java.time.LocalDateTime;

public class TimedHealthCheckResponse {

  private final String id;
  private final LocalDateTime localDateTime;
  private final HealthCheckResponse healthCheckResponse;

  public TimedHealthCheckResponse(String id, LocalDateTime localDateTime, HealthCheckResponse healthCheckResponse) {
    this.id = id;
    this.localDateTime = localDateTime;
    this.healthCheckResponse = healthCheckResponse;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public String getId() {
    return id;
  }

  public HealthCheckResponse getHealthCheckResponse() {
    return healthCheckResponse;
  }

  public URL getUrl() {
    return healthCheckResponse.getUrl();
  }
}
