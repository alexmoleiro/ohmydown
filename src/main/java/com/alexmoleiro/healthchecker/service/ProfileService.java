package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.infrastructure.repositories.EndpointExistingException;

import java.util.List;

import static java.util.Collections.emptySet;

public class ProfileService {

    private final ProfileRepository profileRepository;
    private final HealthCheckRepository healthCheckRepository;
    private EndpointRepository endpointRepository;

    public ProfileService(
            ProfileRepository profileRepository,
            HealthCheckRepository healthCheckRepository,
            EndpointRepository endpointRepository
    ) {
        this.profileRepository = profileRepository;
        this.healthCheckRepository = healthCheckRepository;
        this.endpointRepository = endpointRepository;
    }

    public void addEndpoint(User user, Endpoint endpoint) {
        try {
            endpointRepository.add(endpoint);
        }catch (EndpointExistingException e) {
            endpoint = endpointRepository.get(endpoint.getId()).get();
        }

        profileRepository.addEndpoint(user, endpoint);
    }

    public List<HealthCheckResponses> getResponses(User user) {
        return healthCheckRepository.getResponses(
                profileRepository.get(user).map(Profile::getFollowing)
                        .orElse(emptySet())
        );
    }
}
