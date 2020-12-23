package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;

import java.util.Set;

public class Profile {
  private final User user;
  private Set<Endpoint> endpoints;

  public Profile(User user, Set<Endpoint> endpoints) {

    this.user = user;
    this.endpoints = endpoints;
  }

  public Set<Endpoint> getFollowing() {
    return endpoints;
  }

  public User getUser() {
    return user;
  }
}
