package com.alexmoleiro.healthchecker.core;

public enum CheckResult {
  SSL_CERTIFICATE_ERROR(495);

  private final int value;

  CheckResult(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
