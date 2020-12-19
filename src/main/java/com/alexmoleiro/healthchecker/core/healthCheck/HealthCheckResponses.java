package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.LinkedList;

public class HealthCheckResponses {

  private final Id id;
  private  LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

  public HealthCheckResponses(Id id, HealthCheckResponse response) {
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
