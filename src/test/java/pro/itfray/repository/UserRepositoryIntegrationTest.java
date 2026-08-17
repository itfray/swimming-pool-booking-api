package pro.itfray.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;

class UserRepositoryIntegrationTest extends AbstractDataJpaIntegrationTest {

  @Autowired
  UserRepository repository;

  @BeforeEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("Should save and find a user by the repository")
  void shouldSaveAndFindUser() {
    var userId = UUID.randomUUID();
    var user = new User(userId, Theme.BLACK);
    repository.save(user);

    Optional<User> foundUser = repository.findById(userId);
    assertThat(foundUser).isEqualTo(Optional.of(user));
  }
}
