package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;

import java.net.URL;

public class HealthCheckResponseDto {

  private final HealthCheckResponse response;

  public HealthCheckResponseDto(HealthCheckResponse response) {
    this.response = response;
  }

  public int getStatus() {
    return response.getStatus();
  }

  public URL getUrl() {
    return response.getUrl();
  }

  public long getDelay() {
    return response.getDelay();
  }
}
