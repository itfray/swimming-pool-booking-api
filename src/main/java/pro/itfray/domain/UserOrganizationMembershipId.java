package pro.itfray.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

/** Composite identity for a user-organization membership. */
@Getter
@Embeddable
@NullMarked
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserOrganizationMembershipId implements Serializable {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "organization_id", nullable = false)
  private Long organizationId;
}
