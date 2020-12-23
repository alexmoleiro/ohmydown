package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.InvalidUrlException;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProfileService {

  private final ProfileRepository profileRepository;
  private final HealthCheckRepository healthCheckRepository;

  public ProfileService(ProfileRepository profileRepository, HealthCheckRepository healthCheckRepository) {
    this.profileRepository = profileRepository;
    this.healthCheckRepository = healthCheckRepository;
  }

  public void addUrl(User user, String urlString) {
    URL url;
    try {
      url = new URL(urlString);
    } catch (MalformedURLException e) {
      throw new InvalidUrlException();
    }
    profileRepository.addUrl(user, url);
  }

  public List<HealthCheckResponses> getResponses(User user) {
    return healthCheckRepository.getResponses(new Profile(user).getFollowing());
  }
}
