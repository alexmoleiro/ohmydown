package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.infrastructure.dto.ProfileDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
public class ProfileApi {

  private final OauthService oauthService;
  private final HealthCheckRepository healthCheckRepository;

  public ProfileApi(OauthService oauthService, HealthCheckRepository healthCheckRepository) {
    this.oauthService = oauthService;
    this.healthCheckRepository = healthCheckRepository;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public ProfileDto webStatusResult(@RequestHeader("Token") String token) {
    return new ProfileDto(oauthService.getUser(token), healthCheckRepository);
  }

  @ResponseStatus(value= FORBIDDEN)
  @ExceptionHandler(InvalidTokenException.class)
  public void invalidDomainNames() {
  }

}
