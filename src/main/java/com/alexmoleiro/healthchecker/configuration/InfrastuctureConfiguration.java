package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.SiteResultsRepository;
import com.alexmoleiro.healthchecker.infrastructure.SiteResultsInMemory;
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
  SiteResultsRepository siteResults() {
    return new SiteResultsInMemory();
  }

  @Bean
  HealthCheckerCrawler checkDaemon(HealthChecker healthChecker, SiteResultsRepository siteResultsRepository) {
    return new HealthCheckerCrawler(healthChecker, siteResultsRepository, nThreads);
  }
}