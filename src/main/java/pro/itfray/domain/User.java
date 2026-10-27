package pro.itfray.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Application user entity. */
@Entity
@Getter
@Setter
@Builder
@ToString
@NullMarked
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {

  @Nullable
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter(AccessLevel.NONE)
  @Column(nullable = false, unique = true, updatable = false)
  private UUID uid;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Theme theme = Theme.WHITE;

  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserOrganizationMembership> memberships = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;
    return Objects.equals(uid, user.uid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
}
