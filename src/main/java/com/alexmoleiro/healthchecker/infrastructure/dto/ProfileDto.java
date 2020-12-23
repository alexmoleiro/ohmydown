package com.alexmoleiro.healthchecker.infrastructure.dto;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.service.ProfileService;

import java.util.List;

public class ProfileDto {

  private final ProfileService profileService;
  private final User user;

  public ProfileDto(ProfileService profileService, User user) {
    this.profileService = profileService;
    this.user = user;
  }

  public String getUserId() {
    return user.getId();
  }

  public List<HealthCheckResponses> getResponses() {
    return profileService.getResponses(user);
  }
}
