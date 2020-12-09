package com.alexmoleiro.healthchecker.core;

import java.util.LinkedList;

public class TimedHealthCheckResponses {

  private final Id id;
  private  LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

  public TimedHealthCheckResponses(Id id, HealthCheckResponse response) {
    this.id = id;
    this.healthCheckResponses.addLast(response);
  }

  public Id getId() {
    return id;
  }

  public LinkedList<HealthCheckResponse> getHealthCheckResponse() {
    return healthCheckResponses;
  }

  public void addLast(HealthCheckResponse response) {
    healthCheckResponses.addLast(response);
  }
}
