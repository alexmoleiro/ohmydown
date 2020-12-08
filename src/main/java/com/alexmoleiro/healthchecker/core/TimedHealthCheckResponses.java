package com.alexmoleiro.healthchecker.core;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class TimedHealthCheckResponses {

  private final Id id;
  private final LocalDateTime localDateTime;
  private  LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

  public TimedHealthCheckResponses(Id id, LocalDateTime time, HealthCheckResponse response) {
    this.id = id;
    this.localDateTime = time;
    this.healthCheckResponses.addLast(response);
  }

  public Id getId() {
    return id;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public LinkedList<HealthCheckResponse> getHealthCheckResponse() {
    return healthCheckResponses;
  }
}
