package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.healthCheck.HttpUrl;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

class ProfileRepositoryInMemoryTest {
  ProfileRepositoryInMemory repository = new ProfileRepositoryInMemory();

  @Test
  void shouldAddAUrl() {
    final User user = new User("1", "alex@email.com");

    HttpUrl httpUrlA = new HttpUrl("https://www.a.com");
    HttpUrl httpUrlB = new HttpUrl("https://www.b.com");
    HttpUrl httpUrlC = new HttpUrl("https://www.c.com");

    repository.addEndpoint(user, new Endpoint(httpUrlA));
    repository.addEndpoint(user, new Endpoint(httpUrlB));
    repository.addEndpoint(user, new Endpoint(httpUrlB));
    repository.addEndpoint(user, new Endpoint(httpUrlC));

    Set<Endpoint> following = repository.get(user).get().getFollowing();
    assertThat(following)
        .containsOnly(
                new Endpoint(httpUrlA),
                new Endpoint(httpUrlB),
                new Endpoint(httpUrlC))
            .hasSize(3);
  }

  @Test
  void shouldReturnUser() {
    final Optional<Profile> nonexistent = repository.get(new User("nonexistent", "alex@email.com"));
    assertThat(nonexistent).isEqualTo(empty());
  }
}
