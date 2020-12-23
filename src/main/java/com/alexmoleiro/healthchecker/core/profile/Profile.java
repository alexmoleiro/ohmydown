package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;

import java.util.List;

public class Profile {
  private final User user;
  private final List<Id> ids;

  public Profile(User user, List<Id> ids) {

    this.user = user;
    this.ids = ids;
  }

  public List<Id> getFollowing() {
    return ids;
  }

  public User getUser() {
    return user;
  }
}
