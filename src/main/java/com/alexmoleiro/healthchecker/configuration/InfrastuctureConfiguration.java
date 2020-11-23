package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.SiteChecker;
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
  SiteChecker siteChecker(HttpClient httpClient) {
    return new SiteChecker(httpClient, ofSeconds(seconds));
  }
}
