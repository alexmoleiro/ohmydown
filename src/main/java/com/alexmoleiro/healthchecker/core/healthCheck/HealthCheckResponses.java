package com.alexmoleiro.healthchecker.core.healthCheck;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static java.lang.Float.valueOf;
import static org.springframework.http.HttpStatus.OK;

public class HealthCheckResponses {

  private final Endpoint endpoint;
  private LinkedList<HealthCheckResponse> healthCheckResponses = new LinkedList<>();

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

  public float getUptime() {
    float totalTicks = healthCheckResponses.size();
    float totalNoOk =
        healthCheckResponses.stream()
            .filter(r -> r.getStatus() != OK.value())
            .collect(Collectors.toList())
            .size();
    final DecimalFormat decimalFormat = new DecimalFormat("##.##");
    return valueOf(decimalFormat.format((totalTicks - totalNoOk) / totalTicks * 100));
  }
}
