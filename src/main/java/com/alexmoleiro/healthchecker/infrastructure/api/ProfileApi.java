package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileApi {

  private final OauthService oauthService;

  public ProfileApi(OauthService oauthService) {
    this.oauthService = oauthService;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public User webStatusResult(@RequestHeader("Token") String token) {
    return oauthService.getUser(token);
  }
}
