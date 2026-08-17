package pro.itfray.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  static final UUID ID1 = UUID.randomUUID();
  static final UUID ID2 = UUID.randomUUID();

  @Mock
  UserRepository repository;

  @InjectMocks
  UserServiceImpl service;

  @Test
  @DisplayName("Should create a new user when it does not exist in the repository")
  void shouldCreateUserWhenMissing() {
    when(repository.findById(ID1)).thenReturn(Optional.empty());
    when(repository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

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
}
