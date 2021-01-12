package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.EndpointRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthCheckRepository;
import com.alexmoleiro.healthchecker.core.healthCheck.HealthChecker;
import com.alexmoleiro.healthchecker.core.profile.ProfileRepository;
import com.alexmoleiro.healthchecker.infrastructure.filter.ThrottleFilter;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsLocal;
import com.alexmoleiro.healthchecker.infrastructure.repositories.DomainsRemote;
import com.alexmoleiro.healthchecker.infrastructure.repositories.EndpointInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.HealthChecksInMemory;
import com.alexmoleiro.healthchecker.infrastructure.repositories.ProfileRepositoryInMemory;
import com.alexmoleiro.healthchecker.service.EndpointService;
import com.alexmoleiro.healthchecker.service.HealthCheckerClient;
import com.alexmoleiro.healthchecker.service.HealthCheckerCrawler;
import com.alexmoleiro.healthchecker.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;
import static java.time.Duration.ofSeconds;

@EnableScheduling
@Configuration
public class InfrastuctureConfiguration {

  @Value("${timeout}")
  long seconds;

  @Value("${nthreads}")
  int nThreads;

  @Value("${tokens.daily}")
  int dailyTokens;

  @Value("${tokens.minuteLy}")
  int minutelyTokens;

  @Value("${maxEndpointsPerUser}")
  int maxEndpointsPerUser;

  @Bean
  ThrottleFilter throttleFilter() {
    return new ThrottleFilter(dailyTokens, minutelyTokens);
  }

  @Bean
  HealthChecker httpChecker() {
    return new HealthCheckerClient(
        newBuilder().followRedirects(ALWAYS).build(), ofSeconds(seconds));
  }

  @Bean
  HealthCheckRepository healthCheckRepository() {
    return new HealthChecksInMemory();
  }

  @Bean
  EndpointRepository endpointRepository() {
    return new EndpointInMemory();
  }

  @Bean
  ProfileRepository profileRepository() {
    return new ProfileRepositoryInMemory();
  }

  @Bean
  ProfileService profileService(
      ProfileRepository profileRepository,
      HealthCheckRepository healthCheckRepository,
      EndpointRepository endpointRepository,
      HealthChecker healthChecker) {
    return new ProfileService(
        profileRepository, healthCheckRepository, endpointRepository, healthChecker, maxEndpointsPerUser);
  }

  @Bean
  HealthCheckerCrawler checkDaemon(
      HealthChecker healthChecker, HealthCheckRepository healthCheckRepository) {
    return new HealthCheckerCrawler(healthChecker, healthCheckRepository, nThreads);
  }

  @Bean
  EndpointService endpointService(
      HealthCheckerCrawler healthCheckerCrawler,
      DomainsRepository domainsRepository,
      EndpointRepository endpointRepository) {
    return new EndpointService(healthCheckerCrawler, domainsRepository, endpointRepository);
  }



  @Bean
  @Profile({"pro", "default"})
  DomainsRepository getDomains() {
    return new DomainsRemote();
  }

  @Bean
  @Profile("test")
  DomainsRepository getDomainsTest() {
    return new DomainsLocal();
  }

  @Bean
  @Profile({"fast", "alex"})
  DomainsRepository getDomainsTestFast() {
    return new DomainsLocal();
  }
}
