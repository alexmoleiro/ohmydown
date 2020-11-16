package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;

import java.net.MalformedURLException;
import java.net.URL;

public class WebStatusRequest {
  private URL url;
  private static final String DOMAIN_PATTERN = "^((?!-)[A-Za-z0–9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,20}$";

  public WebStatusRequest(WebStatusRequestDto webStatusRequestDto) throws MalformedURLException {
    setUrl(webStatusRequestDto.getUrl());
  }

  private void setUrl(String urlString) throws MalformedURLException {

    try {
      url = new URL(urlString);
      if (!url.getProtocol().contains("http")) {
        throw new MalformedURLException("No http protocol");
      }
      if (!url.getHost().matches(DOMAIN_PATTERN) && !url.getHost().equals("localhost")) {
        throw new MalformedURLException("Invalid domain name");
      }
    } catch (MalformedURLException e) {
      if (e.getMessage().contains("no protocol")) {
        setUrl("https://" + urlString);
      } else {
        throw new MalformedURLException(e.getMessage());
      }
    }
  }

  public URL getUrl() {
    return url;
  }
}
