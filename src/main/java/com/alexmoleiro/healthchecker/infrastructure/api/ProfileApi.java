package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.infrastructure.dto.ProfileDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileApi {

  private final OauthService oauthService;
  private final HealthCheckResultsRepository healthCheckResultsRepository;

  public ProfileApi(OauthService oauthService, HealthCheckResultsRepository healthCheckResultsRepository) {
    this.oauthService = oauthService;
    this.healthCheckResultsRepository = healthCheckResultsRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public ProfileDto webStatusResult(@RequestHeader("Token") String token) {
    return new ProfileDto(oauthService.getUser(token), healthCheckResultsRepository);
  }
}
