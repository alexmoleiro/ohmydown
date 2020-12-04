package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.SiteResults;
import com.alexmoleiro.healthchecker.service.CheckDaemon;
import com.alexmoleiro.healthchecker.service.HttpChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.time.Duration.ofSeconds;

@Configuration
public class InfrastuctureConfiguration {

  @Value("${timeout}")
  long seconds;

  @Bean
  HttpClient httpClient() {
    return newBuilder().followRedirects(ALWAYS).build();
  }

  @Bean
  HttpChecker siteChecker(HttpClient httpClient) {
    return new HttpChecker(httpClient, ofSeconds(seconds));
  }

  @Bean
  SiteResults siteResults() {
    return new SiteResults();
  }

  @Bean
  CheckDaemon checkDaemon() {
    return new CheckDaemon();
  }
}
