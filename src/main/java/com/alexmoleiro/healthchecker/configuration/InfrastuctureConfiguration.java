package com.alexmoleiro.healthchecker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class InfrastuctureConfiguration {

  @Bean
  HttpClient httpClient() {
    return HttpClient.newBuilder().build();

  }
}
