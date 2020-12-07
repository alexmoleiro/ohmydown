package com.alexmoleiro.healthchecker.core;

public interface HealthChecker {

  HealthCheckResponse check(HealthCheckRequest healthCheckRequest);
}