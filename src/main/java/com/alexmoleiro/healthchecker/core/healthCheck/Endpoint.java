package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Objects;

public class Endpoint {
  private String url;

  public Endpoint(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Endpoint endpoint = (Endpoint) o;
    return Objects.equals(url, endpoint.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url);
  }
}
