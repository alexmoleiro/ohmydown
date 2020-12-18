package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRequest;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.alexmoleiro.healthchecker.core.healthCheck.CheckResultCode.SERVER_TIMEOUT;
import static com.alexmoleiro.healthchecker.core.healthCheck.CheckResultCode.SSL_CERTIFICATE_ERROR;
import static com.alexmoleiro.healthchecker.core.healthCheck.UserAgent.random;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

public class HealthCheckerClient implements HealthChecker {

  private final HttpClient client;
  private final Duration timeout;

  public HealthCheckerClient(HttpClient client, Duration timeout) {
    this.client = client;
    this.timeout = timeout;
  }

  @Override
  public HealthCheckResponse check(HealthCheckRequest healthCheckRequest) {
    int httpStatus;
    final LocalDateTime before = nowUtc();
    try {
      return httpFetch(healthCheckRequest, before);
    } catch (HttpTimeoutException e) {
      httpStatus = SERVER_TIMEOUT.value();
    } catch (ConnectException e) {
      httpStatus = SERVICE_UNAVAILABLE.value();
    } catch (SSLHandshakeException e) {
      httpStatus = SSL_CERTIFICATE_ERROR.value();
    } catch (IOException e) {
      httpStatus = SERVICE_UNAVAILABLE.value();
    } catch (InterruptedException e) {
      httpStatus = SERVICE_UNAVAILABLE.value();
    }

    return new HealthCheckResponse(healthCheckRequest.getUrl(), httpStatus, before, nowUtc());
  }

  private HealthCheckResponse httpFetch(
      HealthCheckRequest healthCheckRequest, LocalDateTime beforeRequest)
      throws IOException, InterruptedException {

    final HttpResponse<Void> send =
        client.send(
            newBuilder()
                .GET()
                .uri(URI.create(healthCheckRequest.getUrl().toString()))
                .setHeader(USER_AGENT, random())
                .timeout(timeout)
                .build(),
            discarding());
    return new HealthCheckResponse(send.uri().toURL(), send.statusCode(), beforeRequest, nowUtc());
  }

  private LocalDateTime nowUtc() {
    return now(systemUTC());
  }
}
