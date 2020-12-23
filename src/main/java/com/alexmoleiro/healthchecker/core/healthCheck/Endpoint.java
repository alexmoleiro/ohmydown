package com.alexmoleiro.healthchecker.core.healthCheck;

import java.util.Objects;

public class Endpoint {
  private final String id;

  public Endpoint(String id) {
    this.id = id;
  }

  public String getValue() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Endpoint endpoint1 = (Endpoint) o;
    return Objects.equals(id, endpoint1.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
