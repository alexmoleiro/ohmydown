package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsLocal;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsRemote;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
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
  HealthCheckRepository siteResults() {
    return new HealthChecksInMemory();
  }

  @Bean
  HealthCheckerCrawler checkDaemon(HealthChecker healthChecker, HealthCheckRepository healthCheckRepository) {
    return new HealthCheckerCrawler(healthChecker, healthCheckRepository, nThreads);
  }

  @Bean
  @Profile("pro")
  DomainsRepository getDomains() {
    return new DomainsRemote();
  }

  @Bean
  @Profile("test")
  DomainsRepository getDomainsTest() {
    return new DomainsLocal();
  }

  @Bean
  @Profile("fast")
  DomainsRepository getDomainsTestFast() {
    return new DomainsLocal();
  }

}