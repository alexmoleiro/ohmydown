package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;

import java.util.Optional;

public interface ProfileRepository {
  void addEndpoint(User user, Endpoint endpoint);
  Optional<Profile> get(User user);
  void deleteEndpoint(User user, Endpoint endpoint);
}
