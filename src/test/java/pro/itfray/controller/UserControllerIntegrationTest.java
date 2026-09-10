package pro.itfray.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;
import pro.itfray.mapper.UserProfileMapper;
import pro.itfray.service.UserService;

class UserControllerIntegrationTest extends AbstractWebMvcIntegrationTest {

  static final String URI = "/v1/users";

  static final String USER_ID = "837d87ac-0960-4a84-b4a6-c46e8ab66bd4";
  static final String ANOTHER_USER_ID = "3a7e327c-b2b4-4894-9cd3-16426b25e970";

  static final String USERNAME = "Test User";
  static final String EMAIL = "test.user@testmail.com";
  static final String FIRST_NAME = "Test";
  static final String LAST_NAME = "User";
  static final boolean HAS_ORGANIZATION = true;
  static final String THEME = Theme.BLACK.name();

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  UserService service;

  @MockitoBean
  UserProfileMapper profileMapper;

  @Nested
  @DisplayName("Create a user")
  class CreateUser {

    @Test
    @DisplayName("Should return unauthorized without an authorization token")
    void shouldReturnUnauthorizedWithoutAuthToken() throws Exception {
      mockMvc.perform(post(URI))
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should create a user and return a user profile")
    void shouldReturnOkAndUserProfile() throws Exception {
      final var user = new User();
      final UUID uid = UUID.fromString(USER_ID);
      when(service.create(uid)).thenReturn(user);
      when(profileMapper.toDto(eq(user), any(Authentication.class), anyBoolean()))
          .thenReturn(UserProfileDto.builder()
              .uid(uid)
              .username(USERNAME)
              .email(EMAIL)
              .firstName(FIRST_NAME)
              .lastName(LAST_NAME)
              .theme(THEME)
              .hasOrganization(HAS_ORGANIZATION)
              .build());

      mockMvc.perform(post(URI).with(jwt()).with(user(USER_ID)))
          .andExpect(status().isCreated())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.uid").value(USER_ID))
          .andExpect(jsonPath("$.username").value(USERNAME))
          .andExpect(jsonPath("$.email").value(EMAIL))
          .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
          .andExpect(jsonPath("$.lastName").value(LAST_NAME))
          .andExpect(jsonPath("$.hasOrganization").value(HAS_ORGANIZATION))
          .andExpect(jsonPath("$.theme").value(THEME));
    }
  }

  @Nested
  @DisplayName("Get a user")
  class GetUser {

    @Test
    @DisplayName("Should return unauthorized without an authorization token")
    void shouldReturnUnauthorizedWithoutAuthToken() throws Exception {
      mockMvc.perform(get(URI + "/" + USER_ID))
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return forbidden with an authorization token of an another user")
    void shouldReturnForbiddenWithAuthTokenOfAnotherUser() throws Exception {
      mockMvc.perform(get(URI + "/" + USER_ID)
              .with(jwt()).with(user(ANOTHER_USER_ID)))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return not found when user does not exist")
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
      when(service.get(UUID.fromString(USER_ID)))
          .thenReturn(Optional.empty());

      mockMvc.perform(get(URI + "/" + USER_ID)
              .with(jwt()).with(user(USER_ID)))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return ok and user profile")
    void shouldReturnOkAndUserProfile() throws Exception {
      final var user = new User();
      final UUID uid = UUID.fromString(USER_ID);
      when(service.get(uid)).thenReturn(Optional.of(user));
      when(profileMapper.toDto(eq(user), any(Authentication.class), anyBoolean()))
          .thenReturn(UserProfileDto.builder()
              .uid(uid)
              .username(USERNAME)
              .email(EMAIL)
              .firstName(FIRST_NAME)
              .lastName(LAST_NAME)
              .theme(THEME)
              .hasOrganization(HAS_ORGANIZATION)
              .build());

      mockMvc.perform(get(URI + "/" + USER_ID)
              .with(jwt()).with(user(USER_ID)))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.uid").value(USER_ID))
          .andExpect(jsonPath("$.username").value(USERNAME))
          .andExpect(jsonPath("$.email").value(EMAIL))
          .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
          .andExpect(jsonPath("$.lastName").value(LAST_NAME))
          .andExpect(jsonPath("$.hasOrganization").value(HAS_ORGANIZATION))
          .andExpect(jsonPath("$.theme").value(THEME));
    }
  }

}
