package com.alexmoleiro.healthchecker.infrastructure;

import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LandingApi {


  private final HealthCheckResultsRepository healthCheckResultsRepository;

  public LandingApi(HealthCheckResultsRepository healthCheckResultsRepository) {
    this.healthCheckResultsRepository = healthCheckResultsRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @PostMapping(value = "/landing-list", produces = "application/json")
  SiteResultsDto webStatusResult() {
    return new SiteResultsDto(healthCheckResultsRepository.getSiteResults());
  }
}
