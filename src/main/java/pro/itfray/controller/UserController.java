package pro.itfray.controller;

import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;
import pro.itfray.mapper.UserProfileMapper;
import pro.itfray.service.UserService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  private final UserService service;
  private final UserProfileMapper profileMapper;

  @PostMapping
  public ResponseEntity<UserProfileDto> createUser(
      Principal principal,
      Authentication authentication) {
    String uid = principal.getName();
    User user = service.create(UUID.fromString(uid));
    UserProfileDto profileDto = profileMapper.toDto(user, authentication, false);
    return ResponseEntity.status(HttpStatus.CREATED).body(profileDto);
  }

  @GetMapping("/{id}")
  @PreAuthorize("authentication.name == #id")
  public ResponseEntity<UserProfileDto> getUser(
      @PathVariable String id,
      Authentication authentication) {
    return service.get(UUID.fromString(id))
        .map(user -> ResponseEntity.ok(profileMapper.toDto(user, authentication, false)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
