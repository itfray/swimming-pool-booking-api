package pro.itfray.service;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import pro.itfray.domain.User;

/** Service that provides operations related to users. */
public interface UserService {

  /**
   * Create a user if it does not already exist.
   *
   * @param uid the user id
   * @return the created or existing user
   */
  User create(@NonNull UUID uid);

  /**
   * Get a user by id.
   *
   * @param uid the user id
   * @return optional user
   */
  Optional<User> get(@NonNull UUID uid);
}
