package com.alexmoleiro.healthchecker.core.healthCheck;

import java.math.BigDecimal;
import java.util.LinkedList;

public class HealthCheckResponses {

  private final Endpoint endpoint;
  private  LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

  public HealthCheckResponses(Endpoint endpoint, HealthCheckResponse response) {
    this.endpoint = endpoint;
    this.healthCheckResponses.addLast(response);
  }

  public HealthCheckResponses(Endpoint endpoint) {
    this.endpoint = endpoint;
  }

  public Endpoint getEndpoint() {
    return endpoint;
  }

  public LinkedList<HealthCheckResponse> getHealthCheckResponse() {
    return healthCheckResponses;
  }

  public void addLast(HealthCheckResponse response) {
    healthCheckResponses.addLast(response);
  }

  public BigDecimal getUptime() {
    return BigDecimal.valueOf(50.00);
  }
}
