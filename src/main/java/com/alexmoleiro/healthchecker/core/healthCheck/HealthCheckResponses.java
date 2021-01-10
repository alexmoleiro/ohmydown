package com.alexmoleiro.healthchecker.core.healthCheck;

import java.text.DecimalFormat;
import java.util.LinkedList;

import static java.lang.Float.valueOf;
import static java.util.stream.Collectors.toList;
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
            .collect(toList())
            .size();
    return valueOf(new DecimalFormat("##.##").format((totalTicks - totalNoOk) / totalTicks * 100));
  }

  public double getAverage() {
    return Double.valueOf(
        new DecimalFormat("##")
            .format(
                healthCheckResponses.stream()
                    .mapToLong(response -> response.getDelay())
                    .summaryStatistics()
                    .getAverage()));
  }
}
