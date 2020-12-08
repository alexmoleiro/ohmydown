package com.alexmoleiro.healthchecker.core;

import java.util.Objects;

public class Id {
  private final String id;

  public Id(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Id id1 = (Id) o;
    return Objects.equals(id, id1.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
