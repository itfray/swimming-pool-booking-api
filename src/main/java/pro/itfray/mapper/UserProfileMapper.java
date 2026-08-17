package pro.itfray.mapper;

import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import pro.itfray.domain.User;
import pro.itfray.dto.UserProfileDto;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

  String USERNAME_CLAIM = "preferred_username";
  String EMAIL_CLAIM = "email";
  String GIVEN_NAME_CLAIM = "given_name";
  String FAMILY_NAME_CLAIM = "family_name";

  default UserProfileDto toDto(
      @NonNull User user,
      Authentication authentication,
      boolean hasOrganization) {
    Objects.requireNonNull(user.getTheme(), "User theme must not be null");

    final var dto = new UserProfileDto();
    if (authentication instanceof JwtAuthenticationToken authToken
        && Objects.nonNull(authToken.getPrincipal())) {
      final var principal = (Jwt) authToken.getPrincipal();
      final Map<String, Object> claims = principal.getClaims();
      if (Objects.nonNull(claims)) {
        dto.setUsername((String) claims.get(USERNAME_CLAIM));
        dto.setEmail((String) claims.get(EMAIL_CLAIM));
        dto.setFirstName((String) claims.get(GIVEN_NAME_CLAIM));
        dto.setLastName((String) claims.get(FAMILY_NAME_CLAIM));
      }
    }

    dto.setUid(user.getUid());
    dto.setTheme(user.getTheme().name());
    dto.setHasOrganization(hasOrganization);
    return dto;
  }
}
