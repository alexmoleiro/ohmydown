package com.alexmoleiro.healthchecker.configuration;

import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.infrastructure.aaa.OauthServiceGoogle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthConfiguration {

  @Value("${googleid}")
  String googleid;

  @Bean
  OauthService getProfileUser() {
    return new OauthServiceGoogle(googleid);
  }
}
