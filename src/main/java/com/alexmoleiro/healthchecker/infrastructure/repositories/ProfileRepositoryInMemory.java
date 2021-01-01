package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ProfileRepositoryInMemory implements ProfileRepository {

  private Map<User, Profile> users = new HashMap<>();

  @Override
  public void addEndpoint(User user, Endpoint endpoint) {
    if (!users.containsKey(user)) {
      final HashSet<Endpoint> urls = new HashSet<>();
      urls.add(endpoint);
      users.put(user, new Profile(user, urls));
    }
    else {
      users.get(user).getFollowing().add(endpoint);
    }
  }

  @Override
  public Optional<Profile> get(User user) {
    return ofNullable(users.get(user));
  }
}
