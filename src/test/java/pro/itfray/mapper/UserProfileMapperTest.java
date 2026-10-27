package pro.itfray.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;

class UserProfileMapperTest {

  UserProfileMapper mapper = Mappers.getMapper(UserProfileMapper.class);

  @Test
  @DisplayName(
      "Should map full user profile when JWT contains username, email, given_name and family_name")
  void shouldMapFullProfile() {
    UUID uid = UUID.randomUUID();
    User user = createUser(uid, Theme.BLACK);

    Map<String, Object> claims = new HashMap<>();
    claims.put(UserProfileMapper.USERNAME_CLAIM, "jdoe");
    claims.put(UserProfileMapper.EMAIL_CLAIM, "jdoe@example.com");
    claims.put(UserProfileMapper.GIVEN_NAME_CLAIM, "John");
    claims.put(UserProfileMapper.FAMILY_NAME_CLAIM, "Doe");

    JwtAuthenticationToken auth = jwtAuthWithClaims(claims);

    UserProfileDto dto = mapper.toDto(user, auth, true);

    assertThat(dto).isNotNull();
    assertThat(dto.getUid()).isEqualTo(uid);
    assertThat(dto.getTheme()).isEqualTo("BLACK");
    assertThat(dto.isHasOrganization()).isTrue();
    assertThat(dto.getUsername()).isEqualTo("jdoe");
    assertThat(dto.getEmail()).isEqualTo("jdoe@example.com");
    assertThat(dto.getFirstName()).isEqualTo("John");
    assertThat(dto.getLastName()).isEqualTo("Doe");
  }

  @Test
  @DisplayName("Should return profile with no JWT claims when authentication is null")
  void shouldHandleNullAuthentication() {
    UUID uid = UUID.randomUUID();
    User user = createUser(uid, Theme.WHITE);

    UserProfileDto dto = mapper.toDto(user, null, false);

    assertThat(dto.getUid()).isEqualTo(uid);
    assertThat(dto.getTheme()).isEqualTo("WHITE");
    assertThat(dto.isHasOrganization()).isFalse();
    assertThat(dto.getUsername()).isNull();
    assertThat(dto.getEmail()).isNull();
    assertThat(dto.getFirstName()).isNull();
    assertThat(dto.getLastName()).isNull();
  }

  @Test
  @DisplayName("Should expose the user's UUID in the DTO")
  void shouldExposeUid() {
    UUID uid = UUID.randomUUID();
    User user = createUser(uid, Theme.WHITE);
    UserProfileDto dto = mapper.toDto(user, null, false);
    assertThat(dto.getUid()).isEqualTo(uid);
    assertThat(dto.getTheme()).isEqualTo("WHITE");
  }

  @Test
  @DisplayName("Should map available claims and leave missing JWT claims as null in DTO")
  void shouldHandleMissingClaims() {
    UUID uid = UUID.randomUUID();
    User user = createUser(uid, Theme.BLACK);

    Map<String, Object> claims = new HashMap<>();
    claims.put(UserProfileMapper.USERNAME_CLAIM, "onlyUsername");
    // email and names are missing intentionally

    JwtAuthenticationToken auth = jwtAuthWithClaims(claims);

    UserProfileDto dto = mapper.toDto(user, auth, false);

    assertThat(dto.getUsername()).isEqualTo("onlyUsername");
    assertThat(dto.getEmail()).isNull();
    assertThat(dto.getFirstName()).isNull();
    assertThat(dto.getLastName()).isNull();
  }

  private static User createUser(UUID uid, Theme theme) {
    return User.builder().uid(uid).theme(theme).build();
  }

  private static JwtAuthenticationToken jwtAuthWithClaims(Map<String, Object> claims) {
    JwtAuthenticationToken auth = mock(JwtAuthenticationToken.class);
    when(auth.getPrincipal())
        .thenReturn(
            new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "none"),
                claims));

    return auth;
  }
}
