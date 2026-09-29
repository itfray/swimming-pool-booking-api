package pro.itfray.service;

import io.micrometer.observation.annotation.ObservationKeyValue;
import io.micrometer.observation.annotation.Observed;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

/** Implementation of {@link UserService} providing user lifecycle operations. */
@Slf4j
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
  @Observed(name = "user.create")
  public User create(@ObservationKeyValue("uid") @NonNull UUID uid) {
    return repository
        .findById(uid)
        .orElseGet(
            () -> {
              try {
                User createdUser = repository.save(new User(uid, Theme.WHITE));
                log.debug("A user '{}' was created", uid);
                return createdUser;
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
  @Observed(name = "user.get")
  public Optional<User> get(@ObservationKeyValue("uid") @NonNull UUID uid) {
    return repository.findById(uid);
  }
}
