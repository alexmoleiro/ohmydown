package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.net.URL;
import java.util.HashMap;

public class ProfileRepositoryInMemory implements ProfileRepository {

  private static final User USER = new User("1", "alex@email.com");
  private HashMap<User, Profile> users = new HashMap<>();

  public ProfileRepositoryInMemory() {

    users.put(USER, new Profile(USER));
  }

  @Override
  public void addUrl(User user, URL url) {}

  //TODO temporarily hardcoded
  @Override
  public Profile get(User user) {
    return users.get(USER);
  }
}
