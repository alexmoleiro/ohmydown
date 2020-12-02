package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.WebStatusRequestDto;

import java.net.MalformedURLException;
import java.net.URL;

public class WebStatusRequest {
  private URL url;
  private static final String DOMAIN_PATTERN =
      "(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]";

  public WebStatusRequest(WebStatusRequestDto webStatusRequestDto) {
    setUrl(webStatusRequestDto.getUrl().toLowerCase());
  }

  private void setUrl(String urlString) {

    try {
      url = new URL(urlString);
      if (!url.getProtocol().contains("http")) {
        throw new MalformedURLException("No http protocol");
      }
      if (!url.getHost().matches(DOMAIN_PATTERN) && !url.getHost().equals("localhost")) {
        throw new WebStatusRequestException(urlString, "Invalid domain name");
      }
    } catch (MalformedURLException e) {
      if (e.getMessage().contains("no protocol")) {
        setUrl("http://" + urlString);
      } else {
        throw new WebStatusRequestException(urlString, e.getMessage());
      }
    }
  }

  public URL getUrl() {
    return url;
  }
}
