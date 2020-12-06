package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;

public interface HealthChecker {

  SiteCheckerResponse check(WebStatusRequest webStatusRequest);
}