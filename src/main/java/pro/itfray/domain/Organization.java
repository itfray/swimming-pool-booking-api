package pro.itfray.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** Organization entity that can be represented by users. */
@Entity
@Getter
@Setter
@Builder
@ToString
@NullMarked
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization")
public class Organization {

  @Id
  @Nullable
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "contact_email", nullable = false)
  private String contactEmail;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "service_street_address", nullable = false)
  private String serviceStreetAddress;

  @Column(name = "service_city", nullable = false)
  private String serviceCity;

  @Column(name = "service_postal_code", nullable = false)
  private String servicePostalCode;

  @Column(name = "service_country", nullable = false)
  private String serviceCountry;

  @Builder.Default
  @ToString.Exclude
  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserOrganizationMembership> memberships = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Organization that = (Organization) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
