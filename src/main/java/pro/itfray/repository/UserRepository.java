package pro.itfray.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.itfray.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}
