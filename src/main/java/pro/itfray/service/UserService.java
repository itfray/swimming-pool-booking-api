package pro.itfray.service;

import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import pro.itfray.domain.User;

public interface UserService {

  User create(@NonNull UUID uid);

  Optional<User> get(@NonNull UUID uid);

}
