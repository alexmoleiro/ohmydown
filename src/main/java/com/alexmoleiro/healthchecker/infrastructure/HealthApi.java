package com.alexmoleiro.healthchecker.infrastructure;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthApi {


  @PostMapping("/status")
  WebStatusResponse webStatusResult(@RequestBody WebStatusRequest webStatusRequest) {
      return new WebStatusResponse(webStatusRequest);
  }
}
