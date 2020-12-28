package com.alexmoleiro.healthchecker.core.healthCheck;

public interface HealthChecker {

  HealthCheckResponse check(HttpUrl httpUrl);
}