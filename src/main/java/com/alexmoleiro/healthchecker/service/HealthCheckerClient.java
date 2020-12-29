package com.alexmoleiro.healthchecker.service;

import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResponse;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;

import static com.alexmoleiro.healthchecker.core.healthCheck.CheckResultCode.SERVER_TIMEOUT;
import static com.alexmoleiro.healthchecker.core.healthCheck.CheckResultCode.SSL_CERTIFICATE_ERROR;
import static com.alexmoleiro.healthchecker.core.healthCheck.UserAgent.random;
import static java.net.URI.create;
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
  public HealthCheckResponse check(HttpUrl httpUrl) {
    int httpStatus;
    final LocalDateTime before = nowUtc();
    try {
      return httpFetch(httpUrl, before);
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

    return new HealthCheckResponse(httpUrl, httpStatus, before, nowUtc());
  }

  private HealthCheckResponse httpFetch(
          HttpUrl httpUrl, LocalDateTime beforeRequest)
      throws IOException, InterruptedException {

    final HttpResponse<Void> send =
        client.send(
            newBuilder()
                .GET()
                .uri(create(httpUrl.toString()))
                .setHeader(USER_AGENT, random())
                .timeout(timeout)
                .build(),
            discarding());
    return new HealthCheckResponse(new HttpUrl(send.uri().toString()), send.statusCode(), beforeRequest, nowUtc());
  }

  private LocalDateTime nowUtc() {
    return now(systemUTC());
  }
}
