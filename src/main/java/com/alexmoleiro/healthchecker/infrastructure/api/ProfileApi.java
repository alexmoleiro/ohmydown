package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.profile.ProfileUser;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileApi {

  private final ProfileUser profileUser;

  public ProfileApi(ProfileUser profileUser) {
    this.profileUser = profileUser;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public User webStatusResult(@RequestHeader("token") String token) {
    return profileUser.getUser(token);
  }
}
