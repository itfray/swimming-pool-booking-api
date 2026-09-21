package pro.itfray.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  @DisplayName("Should return true when comparing same object instance")
  void shouldReturnTrueWhenSameInstance() {
    UUID uid = UUID.randomUUID();
    User user = new User(uid, Theme.WHITE);

    // noinspection EqualsWithItself
    assertThat(user).isEqualTo(user);
  }

  @Test
  @DisplayName("Should return false when comparing with null")
  void shouldReturnFalseWhenComparedWithNull() {
    UUID uid = UUID.randomUUID();
    User user = new User(uid, Theme.WHITE);

    assertThat(user).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return false when comparing with different class")
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    UUID uid = UUID.randomUUID();
    User user = new User(uid, Theme.WHITE);

    // noinspection AssertBetweenInconvertibleTypes
    assertThat(user).isNotEqualTo("not a user");
  }

  @Test
  @DisplayName("Should return true when comparing users with same uid")
  void shouldReturnTrueWhenUidsAreEqual() {
    UUID uid = UUID.randomUUID();
    User user1 = new User(uid, Theme.WHITE);
    User user2 = new User(uid, Theme.BLACK);

    assertThat(user1).isEqualTo(user2);
  }

  @Test
  @DisplayName("Should return false when comparing users with different uids")
  void shouldReturnFalseWhenUidsAreDifferent() {
    User user1 = new User(UUID.randomUUID(), Theme.WHITE);
    User user2 = new User(UUID.randomUUID(), Theme.WHITE);

    assertThat(user1).isNotEqualTo(user2);
  }

  @Test
  @DisplayName("Should return false when comparing user with null uid to user with uid")
  void shouldReturnFalseWhenOneHasNullUid() {
    UUID uid = UUID.randomUUID();
    User user1 = new User(null, Theme.WHITE);
    User user2 = new User(uid, Theme.WHITE);

    assertThat(user1).isNotEqualTo(user2);
  }

  @Test
  @DisplayName("Should return true for hashCode consistency")
  void shouldReturnConsistentHashCode() {
    UUID uid = UUID.randomUUID();
    User user = new User(uid, Theme.WHITE);

    assertThat(user).hasSameHashCodeAs(user);
  }

  @Test
  @DisplayName("Should return same hashCode for users of same class")
  void shouldReturnSameHashCodeForSameClass() {
    User user1 = new User(UUID.randomUUID(), Theme.WHITE);
    User user2 = new User(UUID.randomUUID(), Theme.BLACK);

    assertThat(user1).hasSameHashCodeAs(user2);
  }
}
