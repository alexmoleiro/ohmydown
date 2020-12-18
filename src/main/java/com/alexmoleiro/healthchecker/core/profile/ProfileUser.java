package com.alexmoleiro.healthchecker.core.profile;

public interface ProfileUser {
  User getUser(String token);
}