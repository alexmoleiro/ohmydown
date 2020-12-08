package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.DomainsRepository;
import com.alexmoleiro.healthchecker.core.HealthChecker;
import com.alexmoleiro.healthchecker.core.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.infrastructure.DomainsLocal;
import com.alexmoleiro.healthchecker.infrastructure.DomainsRemote;
import com.alexmoleiro.healthchecker.infrastructure.HealthCheckResultsInMemory;
import com.alexmoleiro.healthchecker.service.HealthCheckerClient;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
  HealthCheckResultsRepository siteResults() {
    return new HealthCheckResultsInMemory();
  }

  @Bean
  HealthCheckerCrawler checkDaemon(HealthChecker healthChecker, HealthCheckResultsRepository healthCheckResultsRepository) {
    return new HealthCheckerCrawler(healthChecker, healthCheckResultsRepository, nThreads);
  }

  @Bean
  @Profile("!test")
  DomainsRepository getDomains() {
    return new DomainsRemote();
  }

  @Bean
  @Profile("test")
  DomainsRepository getDomainsTest() {
    return new DomainsLocal();
  }

}