package com.alexmoleiro.healthchecker.core.healthCheck;

public enum CheckResultCode {
  SSL_CERTIFICATE_ERROR(495),
  SERVER_TIMEOUT(504);

  private final int value;

  CheckResultCode(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
