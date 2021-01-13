package com.alexmoleiro.healthchecker.infrastructure.api;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.healthCheck.InvalidHttpUrlException;
import com.alexmoleiro.healthchecker.core.profile.OauthService;
import com.alexmoleiro.healthchecker.infrastructure.dto.IdDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.ProfileDto;
import com.alexmoleiro.healthchecker.infrastructure.dto.UrlDto;
import com.alexmoleiro.healthchecker.service.MaximumEndpointPerUserExceededException;
import com.alexmoleiro.healthchecker.service.ProfileService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.alexmoleiro.healthchecker.core.healthCheck.CheckResultCode.MAXIMUM_ENDPOINT_PER_USER_EXCEEDED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ProfileApi {

  private final OauthService oauthService;
  private final ProfileService profileService;

  public ProfileApi(OauthService oauthService, ProfileService profileService) {
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
      @RequestBody UrlDto urlDto) {

    profileService.addEndpointToEndpointsAndUserProfile(
            oauthService.getUser(token),
            new Endpoint(new HttpUrl(urlDto.getUrl()))
    );
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @ResponseStatus(OK)
  @DeleteMapping(value = "/profile/deleteurls")
  public void deleteUrl(
          @RequestHeader("Token") String token,
          @RequestBody IdDto idDto) {

    profileService.deleteUrls(oauthService.getUser(token), idDto.getIds());
  }

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(InvalidTokenException.class)
  public void invalidToken() {
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidHttpUrlException.class)
  public void invalidUrl() {
  }

  @ExceptionHandler(MaximumEndpointPerUserExceededException.class)
  public void maximumEndpoints(HttpServletResponse response) {
    response.setStatus(MAXIMUM_ENDPOINT_PER_USER_EXCEEDED.value());
  }
}
