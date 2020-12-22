package com.alexmoleiro.healthchecker.core.profile;

import java.net.URL;

public interface ProfileRepository {
  void addUrl(User user, URL url);
}
