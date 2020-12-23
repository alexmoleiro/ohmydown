package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;

public interface ProfileRepository {
  void addUrl(User user, Endpoint endpoint);
  Profile get(User user);
}
