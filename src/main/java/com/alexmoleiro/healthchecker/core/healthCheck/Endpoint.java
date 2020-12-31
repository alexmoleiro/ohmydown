package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Objects;
import java.util.UUID;

public class Endpoint {

  private HttpUrl httpUrl;
  private String id;

  public Endpoint(HttpUrl httpUrl) {
    this.httpUrl = httpUrl;
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public HttpUrl getUrl() {
    return httpUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Endpoint endpoint = (Endpoint) o;
    return Objects.equals(httpUrl.getUrl().toString(), endpoint.httpUrl.getUrl().toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(httpUrl.getUrl().toString());
  }
}
