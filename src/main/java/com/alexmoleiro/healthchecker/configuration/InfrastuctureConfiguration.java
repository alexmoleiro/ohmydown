package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckResultsRepository;
import com.alexmoleiro.healthchecker.core.profile.ProfileUser;
import com.alexmoleiro.healthchecker.infrastructure.aaa.ProfileUserGoogle;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsLocal;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsRemote;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthCheckResultsInMemory;
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

  @Value("${googleid}")
  String googleid;

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

  @Bean
  ProfileUser getProfileUser() {
    return new ProfileUserGoogle(googleid);
  }
}