package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.HealthCheckRequest;
import com.alexmoleiro.healthchecker.core.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.HealthChecker;
import org.slf4j.Logger;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.alexmoleiro.healthchecker.core.CheckResultCode.SERVER_TIMEOUT;
import static com.alexmoleiro.healthchecker.core.CheckResultCode.SSL_CERTIFICATE_ERROR;
import static com.alexmoleiro.healthchecker.core.UserAgent.random;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.time.Duration.between;
import static java.time.LocalDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

public class HealthCheckerClient implements HealthChecker {

  private final HttpClient client;
  private final Duration timeout;
  private static Logger LOGGER = getLogger(HealthCheckerClient.class);

  public HealthCheckerClient(HttpClient client, Duration timeout) {
    this.client = client;
    this.timeout = timeout;
  }

  @Override
  public HealthCheckResponse check(HealthCheckRequest healthCheckRequest) {
    int httpStatus;
    final LocalDateTime beforeRequest = now();
    try {
      return httpFetch(healthCheckRequest, beforeRequest);
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

    return new HealthCheckResponse(
        healthCheckRequest.getUrl(), httpStatus, between(beforeRequest, now()), now());
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
    return new HealthCheckResponse(
        send.uri().toURL(), send.statusCode(), between(beforeRequest, now()), now());
  }
}