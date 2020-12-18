package com.alexmoleiro.healthchecker.core.healthCheck;

public interface HealthChecker {

  HealthCheckResponse check(HealthCheckRequest healthCheckRequest);
}