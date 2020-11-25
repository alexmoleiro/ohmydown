package com.alexmoleiro.healthchecker.core;

public enum CheckResultCode {
  SSL_CERTIFICATE_ERROR(495);

  private final int value;

  CheckResultCode(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
