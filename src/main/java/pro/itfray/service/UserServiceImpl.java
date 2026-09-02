package pro.itfray.service;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

/** Implementation of {@link UserService} providing user lifecycle operations. */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  /**
   * Create a user if absent.
   *
   * @param uid user id
   * @return created or existing user
   */
  public User create(@NonNull UUID uid) {
    return repository
        .findById(uid)
        .orElseGet(
            () -> {
              try {
                User user = new User(uid, Theme.WHITE);
                return repository.save(user);
              } catch (DataIntegrityViolationException ex) {
                return repository.findById(uid).orElseThrow(() -> ex);
              }
            });
  }

  /**
   * Find a user by id.
   *
   * @param uid user id
   * @return optional user
   */
  public Optional<User> get(@NonNull UUID uid) {
    return repository.findById(uid);
  }
}
