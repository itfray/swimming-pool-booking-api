package pro.itfray.controller;

import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import pro.itfray.api.UserApi;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;
import pro.itfray.mapper.UserProfileMapper;
import pro.itfray.service.UserService;

/** REST controller for user-related endpoints. */
@Validated
@NullMarked
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService service;
  private final UserProfileMapper profileMapper;

  @Override
  public ResponseEntity<UserProfileDto> createUser() {
    Authentication authentication =
        Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication());

    String uid = authentication.getName();
    User user = service.create(UUID.fromString(uid));
    UserProfileDto profileDto = profileMapper.toDto(user, authentication, false);
    return ResponseEntity.status(HttpStatus.CREATED).body(profileDto);
  }

  @Override
  @PreAuthorize("authentication.name == #id.toString()")
  public ResponseEntity<UserProfileDto> getUser(UUID id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return service
        .get(id)
        .map(user -> ResponseEntity.ok(profileMapper.toDto(user, authentication, false)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
