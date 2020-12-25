package com.alexmoleiro.healthchecker.infrastructure.repositories;

import com.alexmoleiro.healthchecker.core.healthCheck.Endpoint;
import com.alexmoleiro.healthchecker.core.profile.Profile;
import com.alexmoleiro.healthchecker.core.profile.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.assertThat;

class ProfileRepositoryInMemoryTest {
  ProfileRepositoryInMemory repository = new ProfileRepositoryInMemory();

  @Test
  void shouldAddAUrl() {
    final User user = new User("1", "alex@email.com");

    repository.addUrl(user, new Endpoint("https://www.a.com"));
    repository.addUrl(user, new Endpoint("https://www.b.com"));
    repository.addUrl(user, new Endpoint("https://www.b.com"));
    repository.addUrl(user, new Endpoint("https://www.c.com"));

    assertThat(repository.get(user).get().getFollowing())
        .isEqualTo(
            of(
                new Endpoint("https://www.a.com"),
                new Endpoint("https://www.b.com"),
                new Endpoint("https://www.c.com")));
  }

  @Test
  void shouldReturnUser() {
    final Optional<Profile> nonexistent = repository.get(new User("nonexistent", "alex@email.com"));
    assertThat(nonexistent).isEqualTo(empty());
  }
}
