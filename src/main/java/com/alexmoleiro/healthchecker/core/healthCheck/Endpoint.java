package com.alexmoleiro.healthchecker.core.healthCheck;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.MessageFormat;
import java.util.Objects;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class Endpoint {

  private static final int THREE_CHARS = 3;
  private HttpUrl httpUrl;
  private String id;
  private String group;

  public Endpoint(HttpUrl httpUrl) {
    this.httpUrl = httpUrl;
    setGroup();
    setId();
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

  public String getGroup() {
    return group;
  }

  private void setId() {
    this.id = format("{0}-{1}", group, randomAlphanumeric(THREE_CHARS));
  }

  private void setGroup() {
    String[] domain = httpUrl.getUrl().getHost().split("\\.");
    group = format("{0}.{1}", domain[domain.length - 2], domain[domain.length - 1]);
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
