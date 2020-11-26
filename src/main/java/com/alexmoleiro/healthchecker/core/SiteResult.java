package com.alexmoleiro.healthchecker.core;

import com.alexmoleiro.healthchecker.infrastructure.SiteStatus;

import java.net.URL;

public class SiteResult {
  URL url;
  long delay;
  SiteStatus siteStatus;

  public SiteResult(URL url, long delay, SiteStatus siteStatus) {
    this.url = url;
    this.delay = delay;
    this.siteStatus = siteStatus;
  }

  public URL getUrl() {
    return url;
  }

  public long getDelay() {
    return delay;
  }

  public String getSiteStatus() {
    return siteStatus.toString();
  }
}
