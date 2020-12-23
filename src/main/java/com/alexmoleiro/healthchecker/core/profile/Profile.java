package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;

import java.util.Set;

public class Profile {
  private final User user;
  private Set<Id> ids;

  public Profile(User user, Set<Id> ids) {

    this.user = user;
    this.ids = ids;
  }

  public Set<Id> getFollowing() {
    return ids;
  }

  public User getUser() {
    return user;
  }
}
