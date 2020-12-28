package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.healthCheck.InvalidHttpUrlException;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.infrastructure.dto.ProfileDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.WebStatusRequestDto;
import com.alexmoleiro.healthchecker.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ProfileApi {

  private final OauthService oauthService;
  private final ProfileService profileService;

  public ProfileApi(OauthService oauthService, ProfileService profileService
  ) {
    this.oauthService = oauthService;
    this.profileService = profileService;
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping(value = "/profile", produces = "application/json")
  public ProfileDto profile(@RequestHeader("Token") String token) {
    return new ProfileDto(profileService, oauthService.getUser(token));
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseStatus(CREATED)
  @PostMapping(value = "/profile/addurl", produces = "application/json")
  public void addUrl(
      @RequestHeader("Token") String token,
      @RequestBody WebStatusRequestDto webStatusRequestDto) {

    profileService.addUrl(oauthService.getUser(token), new HttpUrl(webStatusRequestDto.getUrl()));
  }

  @ResponseStatus(value= FORBIDDEN)
  @ExceptionHandler(InvalidTokenException.class)
  public void invalidToken() {
  }

  @ResponseStatus(value= BAD_REQUEST)
  @ExceptionHandler(InvalidHttpUrlException.class)
  public void invalidUrl() {
  }

}
