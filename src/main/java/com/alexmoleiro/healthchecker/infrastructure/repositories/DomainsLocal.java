package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;

import java.util.List;

import static java.util.List.of;

public class DomainsLocal implements DomainsRepository {

  @Override
  public List<String> getDomains()  {

    return of("www.as.com", "www.sport.com");
  }
}
