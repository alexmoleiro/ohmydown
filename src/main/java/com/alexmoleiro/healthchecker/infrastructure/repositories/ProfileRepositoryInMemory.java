package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ProfileRepositoryInMemory implements ProfileRepository {

  private Map<User, Profile> users = new HashMap<>();

  @Override
  public void addUrl(User user, URL url) {
    if (!users.containsKey(user)) {
      final HashSet<Id> urls = new HashSet<>();
      urls.add(new Id(url.toString()));
      users.put(user, new Profile(user, urls));
    }
    else {
      users.get(user).getFollowing().add(new Id(url.toString()));
    }
  }

  @Override
  public Profile get(User user) {
    return users.get(user);
  }
}
