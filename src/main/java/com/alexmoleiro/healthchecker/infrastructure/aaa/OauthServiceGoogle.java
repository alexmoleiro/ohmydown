package com.alexmoleiro.healthchecker.infrastructure.aaa;

import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.core.profile.User;
import com.alexmoleiro.healthchecker.infrastructure.api.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Collections.singletonList;
/**
    "at_hash" -> ""
    "aud" -> "-cmkp5lvq2g3fb82t0gibr2ai10l75guo.apps.googleusercontent.com"
    "azp" -> "-cmkp5lvq2g3fb82t0gibr2ai10l75guo.apps.googleusercontent.com"
    "email" -> "alejandro@dominio.com"
    "email_verified" -> {Boolean@7597} true
    "exp" -> {Long@7599} 1608298282
    "hd" -> "hola.com"
    "iat" -> {Long@7603}
    "iss" -> "accounts.google.com"
    "jti" -> ""
    "sub" -> "1002162"
    "name" -> "Alejandro  "
    "picture" -> "https://lh3.googleusercontent.com/a-/AOh14GhM0XSn0iJKH2BqhAJNlfHfC2Yp7KUrlazrEkGa=s96-c"
    "given_name" -> "Nombre"
    "family_name" -> "Apellido"
    "locale" -> "en-GB"
*/

public class OauthServiceGoogle implements OauthService {

  GoogleIdTokenVerifier verifier;


  public OauthServiceGoogle(String googleid) {
    verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(singletonList(googleid))
            .build();
  }

  @Override
  public User getUser(String token) {
    GoogleIdToken googleIdToken;
    try {
      googleIdToken = verifier.verify(token);
    } catch (IllegalArgumentException | GeneralSecurityException | IOException e) {
      throw new InvalidTokenException();
    }
    return new User(
        (String) googleIdToken.getPayload().get("sub"),
        (String) googleIdToken.getPayload().get("email")
    );
  }
}
