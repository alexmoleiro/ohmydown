package com.alexmoleiro.healthchecker.infrastructure.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Collections.singletonList;

@RestController
public class ProfileApi {

  private static final String ID = "471923655035-cmkp5lvq2g3fb82t0gibr2ai10l75guo.apps.googleusercontent.com";

  @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/profile", produces = "application/json")
    public String webStatusResult() throws GeneralSecurityException, IOException {

      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
          .setAudience(singletonList(ID))
          .build();

    String idTokenString = "";
    GoogleIdToken idToken = verifier.verify(idTokenString);

    return  """
          {"name":"%s"}
          """.formatted(idToken.getPayload().get("given_name"));
    }

  }