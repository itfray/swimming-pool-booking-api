package pro.itfray.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  static final UUID ID1 = UUID.randomUUID();
  static final UUID ID2 = UUID.randomUUID();
  static final UUID ID3 = UUID.randomUUID();

  @Mock UserRepository repository;

  @InjectMocks UserServiceImpl service;

  @Test
  @DisplayName("Should create a new user when it does not exist in the repository")
  void shouldCreateUserWhenMissing() {
    when(repository.findById(ID1)).thenReturn(Optional.empty());
    when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    User user = service.create(ID1);

    assertThat(user.getUid()).isEqualTo(ID1);
    assertThat(user.getTheme()).isEqualTo(Theme.WHITE);
  }

  @Test
  @DisplayName("Should return an existing user when it exists in the repository")
  void shouldReturnExistingUser() {
    User existing = new User(ID2, Theme.BLACK);
    when(repository.findById(ID2)).thenReturn(Optional.of(existing));

    User user = service.create(ID2);

    assertThat(user).isSameAs(existing);
  }

  @Test
  @DisplayName("Should handle DataIntegrityViolationException by returning user from repository")
  void shouldHandleDataIntegrityViolationException() {
    User createdUser = new User(ID3, Theme.WHITE);
    when(repository.findById(ID3))
        .thenReturn(Optional.empty())
        .thenReturn(Optional.of(createdUser));
    when(repository.save(any()))
        .thenThrow(new DataIntegrityViolationException("Duplicate key value"));

    User user = service.create(ID3);

    assertThat(user).isSameAs(createdUser);
  }

  @Test
  @DisplayName(
      "Should throw exception when DataIntegrityViolationException occurs and user not found")
  void shouldThrowExceptionWhenUserNotFoundAfterIntegrityViolation() {
    when(repository.findById(ID1)).thenReturn(Optional.empty());
    when(repository.save(any()))
        .thenThrow(new DataIntegrityViolationException("Duplicate key value"));

    assertThatThrownBy(() -> service.create(ID1))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("Should find an existing user by id")
  void shouldFindExistingUserById() {
    User existing = new User(ID2, Theme.BLACK);
    when(repository.findById(ID2)).thenReturn(Optional.of(existing));

    Optional<User> user = service.get(ID2);

    assertThat(user).contains(existing);
  }

  @Test
  @DisplayName("Should return empty optional when user not found")
  void shouldReturnEmptyWhenUserNotFound() {
    when(repository.findById(ID1)).thenReturn(Optional.empty());

    Optional<User> user = service.get(ID1);

    assertThat(user).isEmpty();
  }

  @Test
  @DisplayName("Should throw NullPointerException when create is called with null uid")
  void shouldThrowNullPointerExceptionWhenCreateWithNullUid() {
    // noinspection DataFlowIssue
    assertThatThrownBy(() -> service.create(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("Should throw NullPointerException when get is called with null uid")
  void shouldThrowNullPointerExceptionWhenGetWithNullUid() {
    // noinspection DataFlowIssue
    assertThatThrownBy(() -> service.get(null)).isInstanceOf(NullPointerException.class);
  }
}
