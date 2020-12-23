package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;

public interface ProfileRepository {
  void addUrl(User user, Id id);
  Profile get(User user);
}
