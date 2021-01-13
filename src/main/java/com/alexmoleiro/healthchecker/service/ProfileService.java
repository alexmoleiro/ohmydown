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
    isUserExceedingNumberOfEndpoints(user);
    healthCheckRepository.add(endpoint, healthChecker.check(endpoint.getHttpUrl()));
    endpointRepository.add(endpoint);
    profileRepository.addEndpoint(user, endpoint);
  }

  private void isUserExceedingNumberOfEndpoints(User user) {
    profileRepository
        .get(user)
        .ifPresent(
            u -> {
              if (u.getFollowing().size() + 1 > maxEndpointsPerUserLimit) {
                throw new MaximumEndpointPerUserExceededException();
              }
            });
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
