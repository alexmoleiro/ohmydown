package com.alexmoleiro.healthchecker.core.profile;

import com.alexmoleiro.healthchecker.core.healthCheck.Id;

import java.util.List;

import static java.util.List.of;

public class Profile {
  private final User user;

  public Profile(User user) {

    this.user = user;
  }

  public List<Id> getFollowing() {
    return of(new Id("amazon.com"), new Id("sport.it"), new Id("joindrover.com"));
  }

  public User getUser() {
    return user;
  }
}
