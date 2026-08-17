package pro.itfray.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {

  @Id
  private UUID uid;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Theme theme = Theme.WHITE;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;
    return uid != null && uid.equals(user.uid);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
