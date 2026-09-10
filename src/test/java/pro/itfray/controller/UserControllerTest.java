package pro.itfray.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;
import pro.itfray.mapper.UserProfileMapper;
import pro.itfray.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  static final String USER_ID = "837d87ac-0960-4a84-b4a6-c46e8ab66bd4";

  @Mock UserService service;

  @Mock UserProfileMapper profileMapper;

  @Mock Principal principal;

  @Mock Authentication authentication;

  @InjectMocks UserController controller;

  @Test
  @DisplayName("Should create a new user and return HTTP 201 with profile")
  void shouldCreateNewUserAndReturnCreatedProfile() {
    final var user = new User();
    final UUID uid = UUID.fromString(USER_ID);

    when(principal.getName()).thenReturn(USER_ID);
    when(service.create(uid)).thenReturn(user);
    final UserProfileDto dto = UserProfileDto.builder().uid(uid).build();
    when(profileMapper.toDto(user, authentication, false)).thenReturn(dto);

    ResponseEntity<UserProfileDto> response = controller.createUser(principal, authentication);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(dto);
  }

  @Test
  @DisplayName("Should return HTTP 200 and user profile when user exists")
  void shouldReturnOkAndUserProfileWhenUserExists() {
    final var user = new User();
    final UUID uid = UUID.fromString(USER_ID);

    when(service.get(uid)).thenReturn(Optional.of(user));
    final UserProfileDto dto = UserProfileDto.builder().uid(uid).build();
    when(profileMapper.toDto(user, authentication, false)).thenReturn(dto);

    ResponseEntity<UserProfileDto> response = controller.getUser(USER_ID, authentication);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(dto);
  }

  @Test
  @DisplayName("Should return HTTP 404 when requested user does not exist")
  void shouldReturnNotFoundWhenUserDoesNotExist() {
    final UUID uid = UUID.fromString(USER_ID);

    when(service.get(uid)).thenReturn(Optional.empty());

    ResponseEntity<UserProfileDto> response = controller.getUser(USER_ID, authentication);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNull();
  }
}
