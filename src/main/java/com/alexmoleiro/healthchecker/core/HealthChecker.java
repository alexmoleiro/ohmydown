package com.alexmoleiro.healthchecker.core;

public interface HealthChecker {

  SiteCheckerResponse check(WebStatusRequest webStatusRequest);
}