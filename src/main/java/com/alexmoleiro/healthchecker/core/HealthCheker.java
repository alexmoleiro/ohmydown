package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.SiteCheckerResponse;

public interface HealthCheker {

  SiteCheckerResponse check(WebStatusRequest webStatusRequest);
}