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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  public User create(@NonNull UUID uid) {
    return repository.findById(uid)
        .orElseGet(() -> {
          try {
            User user = new User(uid, Theme.WHITE);
            return repository.save(user);
          } catch (DataIntegrityViolationException ex) {
            return repository.findById(uid).orElseThrow(() -> ex);
          }
        });
  }

  public Optional<User> get(@NonNull UUID uid) {
    return repository.findById(uid);
  }
}
