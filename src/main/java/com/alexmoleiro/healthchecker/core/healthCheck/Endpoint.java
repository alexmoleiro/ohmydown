package com.alexmoleiro.healthchecker.core.healthCheck;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.UUID;

import static java.text.MessageFormat.format;

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

  @JsonIgnore
  public HttpUrl getHttpUrl() {
    return httpUrl;
  }

  public String getUrl() {
    return httpUrl.toString();
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

  public String getGroup() {
    String[] domain = httpUrl.getUrl().getHost().split("\\.");
    return format("{0}.{1}", domain[domain.length - 2], domain[domain.length - 1]);
  }
}
