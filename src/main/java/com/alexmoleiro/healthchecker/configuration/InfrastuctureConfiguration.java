package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.service.SiteChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.newBuilder;

@Configuration
public class InfrastuctureConfiguration {

  @Bean
  HttpClient httpClient() {
    return newBuilder().followRedirects(ALWAYS).build();
  }

  @Bean
  SiteChecker siteChecker(HttpClient httpClient) {
    return new SiteChecker(httpClient);
  }
}
