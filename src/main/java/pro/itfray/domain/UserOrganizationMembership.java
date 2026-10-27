package pro.itfray.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;

/** Membership linking a user to an organization. */
@Entity
@Getter
@ToString
@NullMarked
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "user_organization_membership")
public class UserOrganizationMembership {

  @EmbeddedId private UserOrganizationMembershipId id;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
  private User user;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_id", nullable = false, insertable = false, updatable = false)
  private Organization organization;

  /**
   * Create a membership linking a user to an organization.
   *
   * @param user user to assign to organization
   * @param organization organization to assign user to
   */
  public UserOrganizationMembership(User user, Organization organization) {
    this.id =
        new UserOrganizationMembershipId(
            Objects.requireNonNull(user.getId(), "user id must not be null"),
            Objects.requireNonNull(organization.getId(), "organization id must not be null"));

    this.user = user;
    this.organization = organization;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserOrganizationMembership that = (UserOrganizationMembership) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
