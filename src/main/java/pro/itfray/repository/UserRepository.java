package pro.itfray.repository;

import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.itfray.domain.User;

/** Repository for User entities. */
@NullMarked
public interface UserRepository extends JpaRepository<User, Long> {

  /** Find a user by its natural UUID identifier. */
  Optional<User> findByUid(UUID uid);
}
