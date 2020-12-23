package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.LinkedList;

public class HealthCheckResponses {

  private final Endpoint endpoint;
  private  LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

  public HealthCheckResponses(Endpoint endpoint, HealthCheckResponse response) {
    this.endpoint = endpoint;
    this.healthCheckResponses.addLast(response);
  }

  public Endpoint getId() {
    return endpoint;
  }

  public LinkedList<HealthCheckResponse> getHealthCheckResponse() {
    return healthCheckResponses;
  }

  public void addLast(HealthCheckResponse response) {
    healthCheckResponses.addLast(response);
  }
}
