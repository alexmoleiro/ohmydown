package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.SiteResults;
import com.alexmoleiro.healthchecker.service.HealthCheckerClient;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  HealthChecker httpChecker() {
    return new HealthCheckerClient(newBuilder().followRedirects(ALWAYS).build(), ofSeconds(seconds));
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