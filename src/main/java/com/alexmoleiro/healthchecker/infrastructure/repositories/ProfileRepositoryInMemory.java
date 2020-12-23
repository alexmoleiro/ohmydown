package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProfileRepositoryInMemory implements ProfileRepository {

  private Map<User, Profile> users = new HashMap<>();

  @Override
  public void addUrl(User user, Id id) {
    if (!users.containsKey(user)) {
      final HashSet<Id> urls = new HashSet<>();
      urls.add(id);
      users.put(user, new Profile(user, urls));
    }
    else {
      users.get(user).getFollowing().add(id);
    }
  }

  @Override
  public Profile get(User user) {
    return users.get(user);
  }
}
