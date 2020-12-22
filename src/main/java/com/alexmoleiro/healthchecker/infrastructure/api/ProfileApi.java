package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.InvalidUrlException;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.infrastructure.dto.ProfileDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.ProfileService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
public class ProfileApi {

  private final OauthService oauthService;
  private final HealthCheckRepository healthCheckRepository;
  private final ProfileService profileService;

  public ProfileApi(OauthService oauthService,
                    HealthCheckRepository healthCheckRepository,
                    ProfileService profileService
  ) {
    this.oauthService = oauthService;
    this.healthCheckRepository = healthCheckRepository;
    this.profileService = profileService;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public ProfileDto profile(@RequestHeader("Token") String token) {
    return new ProfileDto(oauthService.getUser(token), healthCheckRepository);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseStatus(CREATED)
  @PostMapping(value = "/profile/addurl", produces = "application/json")
  public void addUrl(
      @RequestHeader("Token") String token,
      @RequestBody WebStatusRequestDto webStatusRequestDto) {

    profileService.addUrl(oauthService.getUser(token), webStatusRequestDto.getUrl());
  }

  @ResponseStatus(value= FORBIDDEN)
  @ExceptionHandler(InvalidTokenException.class)
  public void invalidToken() {
  }

  @ResponseStatus(value= BAD_REQUEST)
  @ExceptionHandler(InvalidUrlException.class)
  public void invalidUrl() {
  }

}
