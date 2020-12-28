package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Objects;

public class Endpoint {

  private HttpUrl httpUrl;
  private String id;

  public Endpoint(HttpUrl httpUrl) {
    this.httpUrl = httpUrl;
    this.id = httpUrl.toString();
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Endpoint endpoint = (Endpoint) o;
    return Objects.equals(id, endpoint.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
