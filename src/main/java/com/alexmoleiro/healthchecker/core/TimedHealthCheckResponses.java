package com.alexmoleiro.healthchecker.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimedHealthCheckResponses {

  private final Id id;
  private final LocalDateTime localDateTime;
  private  List<HealthCheckResponse> healthCheckResponses = new ArrayList<>();

  public TimedHealthCheckResponses(Id id, LocalDateTime time, HealthCheckResponse response) {
    this.id = id;
    this.localDateTime = time;
    this.healthCheckResponses.add(response);
  }

  public Id getId() {
    return id;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public List<HealthCheckResponse> getHealthCheckResponse() {
    return healthCheckResponses;
  }
}
