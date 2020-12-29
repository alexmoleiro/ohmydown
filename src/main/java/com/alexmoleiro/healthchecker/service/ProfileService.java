package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponses;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.util.List;

import static java.util.Collections.emptySet;

public class ProfileService {

    private final ProfileRepository profileRepository;
    private final HealthCheckRepository healthCheckRepository;

    public ProfileService(
            ProfileRepository profileRepository, HealthCheckRepository healthCheckRepository) {
        this.profileRepository = profileRepository;
        this.healthCheckRepository = healthCheckRepository;
    }

    public void addUrl(User user, HttpUrl httpUrl) {
        profileRepository.addUrl(user, new Endpoint(httpUrl));
    }

    public List<HealthCheckResponses> getResponses(User user) {
        return healthCheckRepository.getResponses(
                profileRepository.get(user).map(profile -> profile.getFollowing())
                        .orElse(emptySet())
        );
    }
}
