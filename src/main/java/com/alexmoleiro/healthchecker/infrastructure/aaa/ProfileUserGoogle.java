package com.alexmoleiro.healthchecker.infrastructure.aaa;

import com.alexmoleiro.healthchecker.core.ProfileUser;
import com.alexmoleiro.healthchecker.infrastructure.api.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Collections.singletonList;

public class ProfileUserGoogle implements ProfileUser {

  GoogleIdTokenVerifier verifier;

  public ProfileUserGoogle(String googleid) {
    verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(singletonList(googleid))
            .build();
  }

  @Override
  public String getName(String token) {
    GoogleIdToken googleIdToken;
    String given_name;
    try {
      googleIdToken = verifier.verify(token);
      given_name = (String) googleIdToken.getPayload().get("given_name");
    } catch (IllegalArgumentException | GeneralSecurityException | IOException e) {
      throw new InvalidTokenException();
    }
    return given_name;
  }
}
