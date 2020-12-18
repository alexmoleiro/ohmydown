package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.DomainsRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static java.nio.file.Files.lines;
import static java.nio.file.Path.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.ResourceUtils.getFile;

public class DomainsLocalResources implements DomainsRepository {

  public static final String DOMAINS_FILE = "classpath:sites/domains-english.md";

  @Override
  public List<String> getDomains() {
    List<String> list = null;

    try {
      list = lines(of(getFile(DOMAINS_FILE).getAbsolutePath())).collect(toList());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return list;
  }
}
