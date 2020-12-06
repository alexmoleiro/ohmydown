package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.SiteResults;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import com.alexmoleiro.healthchecker.service.HealthChecker;
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

  @Value("${nthreads}")
  int nThreads;

  @Bean
  HttpClient httpClient() {
    return newBuilder().followRedirects(ALWAYS).build();
  }

  @Bean
  HealthChecker httpChecker(HttpClient httpClient) {
    return new HealthChecker(httpClient, ofSeconds(seconds));
  }

  @Bean
  SiteResults siteResults() {
    return new SiteResults();
  }

  @Bean
  HealthCheckerCrawler checkDaemon(HealthChecker healthChecker, SiteResults siteResults) {
    return new HealthCheckerCrawler(healthChecker, siteResults, nThreads);
  }
}
