package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;

import java.util.Optional;

public interface ProfileRepository {
  void addUrl(User user, Endpoint endpoint);
  Optional<Profile> get(User user);
}
