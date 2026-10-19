package pro.itfray.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

/** DTO that represents a user profile returned by the API. */
@Data
@Builder
@NullUnmarked
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

  private UUID uid;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String theme;
  private boolean hasOrganization;
}
