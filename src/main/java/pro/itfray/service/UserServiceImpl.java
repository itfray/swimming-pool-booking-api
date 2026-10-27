package pro.itfray.service;

import io.micrometer.observation.annotation.ObservationKeyValue;
import io.micrometer.observation.annotation.Observed;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

/** Implementation of {@link UserService} providing user lifecycle operations. */
@Slf4j
@Service
@NullMarked
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
  public User create(@ObservationKeyValue("uid") UUID uid) {
    return repository
        .findByUid(uid)
        .orElseGet(
            () -> {
              try {
                User createdUser = repository.save(User.builder().uid(uid).build());
                log.debug("A user '{}' was created", uid);
                return createdUser;
              } catch (DataIntegrityViolationException ex) {
                return repository.findByUid(uid).orElseThrow(() -> ex);
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
  public Optional<User> get(@ObservationKeyValue("uid") UUID uid) {
    return repository.findByUid(uid);
  }
}
