package com.alexmoleiro.healthchecker.core.profile;

public interface OauthService {
  User getUser(String token);
}