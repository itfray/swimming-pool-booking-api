package pro.itfray.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;

class UserRepositoryIntegrationTest extends AbstractDataJpaIntegrationTest {

  @Autowired UserRepository repository;

  @BeforeEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("Should save and find a user by the repository")
  void shouldSaveAndFindUser() {
    var userId = UUID.randomUUID();
    var user = User.builder().uid(userId).theme(Theme.BLACK).build();

    User savedUser = repository.saveAndFlush(user);

    assertThat(savedUser.getId()).isNotNull();
    Optional<User> foundUser = repository.findByUid(userId);
    assertThat(foundUser).contains(savedUser);
    assertThat(repository.findById(savedUser.getId())).contains(savedUser);
  }

  @Test
  @DisplayName("Should enforce unique user UIDs")
  void shouldEnforceUniqueUid() {
    UUID uid = UUID.randomUUID();
    repository.saveAndFlush(user(uid));

    User sameUser = user(uid);
    assertThatThrownBy(() -> repository.saveAndFlush(sameUser))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("Should enforce non-null user UIDs")
  void shouldEnforceNonNullUid() {

    User user = user(null);
    assertThatThrownBy(() -> repository.saveAndFlush(user))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @SuppressWarnings("DataFlowIssue")
  private User user(@Nullable UUID uid) {
    return User.builder().uid(uid).build();
  }
}
