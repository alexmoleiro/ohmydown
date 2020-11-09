package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.infrastructure.SiteChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class InfrastuctureConfiguration {

  @Bean
  HttpClient httpClient() {
    return HttpClient.newBuilder().build();
  }

  @Bean
  SiteChecker siteChecker(HttpClient httpClient) {
    return new SiteChecker(httpClient);
  }
}
