package pro.itfray.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.itfray.domain.User;

/** Repository for User entities. */
public interface UserRepository extends JpaRepository<User, UUID> {}
