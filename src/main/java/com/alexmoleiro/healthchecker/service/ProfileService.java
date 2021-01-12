package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

public class ProfileService {

  private final ProfileRepository profileRepository;
  private final HealthCheckRepository healthCheckRepository;
  private EndpointRepository endpointRepository;
  private final HealthChecker healthChecker;
  private final int maxEndpointsPerUserLimit;

  public ProfileService(
      ProfileRepository profileRepository,
      HealthCheckRepository healthCheckRepository,
      EndpointRepository endpointRepository,
      HealthChecker healthChecker,
      int maxEndpointsPerUserLimit) {
    this.profileRepository = profileRepository;
    this.healthCheckRepository = healthCheckRepository;
    this.endpointRepository = endpointRepository;
    this.healthChecker = healthChecker;
    this.maxEndpointsPerUserLimit = maxEndpointsPerUserLimit;
  }

  public void addEndpointToEndpointsAndUserProfile(User user, Endpoint endpoint) {
    if (isUserExceedingNumberOfEndpoints(user)) {
      throw new MaximumEndpointPerUserExceededException();
    }
    endpointRepository.add(endpoint);
    healthCheckRepository.add(endpoint, healthChecker.check(endpoint.getHttpUrl()));
    profileRepository.addEndpoint(user, endpoint);
  }

  private Boolean isUserExceedingNumberOfEndpoints(User user) {
    return profileRepository
        .get(user)
        .map(u -> u.getFollowing().size() + 1 > maxEndpointsPerUserLimit)
        .orElse(false);
  }

  public List<HealthCheckResponses> getResponses(User user) {
    return healthCheckRepository.getResponses(
        profileRepository.get(user).map(Profile::getFollowing).orElse(emptySet()));
  }

  public void deleteUrls(User user, Set<String> ids) {
    ids.forEach(
        id ->
            endpointRepository
                .get(id)
                .ifPresent(endpoint -> profileRepository.deleteEndpoint(user, endpoint)));
  }
}
