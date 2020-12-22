package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.core.profile.User;

import java.net.URL;

public class ProfileRepositoryInMemory implements ProfileRepository {
  @Override
  public void addUrl(User user, URL url) {

  }
}
