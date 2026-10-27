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
    User user = User.builder().uid(uid).theme(Theme.WHITE).build();

    // noinspection EqualsWithItself
    assertThat(user).isEqualTo(user);
  }

  @Test
  @DisplayName("Should return false when comparing with null")
  void shouldReturnFalseWhenComparedWithNull() {
    UUID uid = UUID.randomUUID();
    User user = User.builder().uid(uid).theme(Theme.WHITE).build();

    assertThat(user).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return false when comparing with different class")
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    UUID uid = UUID.randomUUID();
    User user = User.builder().uid(uid).theme(Theme.WHITE).build();

    // noinspection AssertBetweenInconvertibleTypes
    assertThat(user).isNotEqualTo("not a user");
  }

  @Test
  @DisplayName("Should return true when comparing users with same uid")
  void shouldReturnTrueWhenUidsAreEqual() {
    UUID uid = UUID.randomUUID();
    User user1 = User.builder().uid(uid).theme(Theme.WHITE).build();
    User user2 = User.builder().uid(uid).theme(Theme.BLACK).build();

    assertThat(user1).isEqualTo(user2);
  }

  @Test
  @DisplayName("Should return false when comparing users with different uids")
  void shouldReturnFalseWhenUidsAreDifferent() {
    User user1 = User.builder().uid(UUID.randomUUID()).theme(Theme.WHITE).build();
    User user2 = User.builder().uid(UUID.randomUUID()).theme(Theme.WHITE).build();

    assertThat(user1).isNotEqualTo(user2);
  }

  @Test
  @DisplayName("Should return true for hashCode consistency")
  void shouldReturnConsistentHashCode() {
    UUID uid = UUID.randomUUID();
    User user = User.builder().uid(uid).theme(Theme.WHITE).build();

    assertThat(user).hasSameHashCodeAs(user);
  }

  @Test
  @DisplayName("Should return hashCode for user's uid")
  void shouldReturnHashCodeForUserUid() {
    var uid = UUID.randomUUID();
    User user1 = User.builder().uid(uid).theme(Theme.WHITE).build();
    User user2 = User.builder().uid(uid).theme(Theme.BLACK).build();

    assertThat(user1).hasSameHashCodeAs(user2);
  }
}
