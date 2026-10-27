package pro.itfray.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.itfray.domain.Organization;

class OrganizationRepositoryIntegrationTest extends AbstractDataJpaIntegrationTest {

  @Autowired private OrganizationRepository repository;

  @BeforeEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("Should save and find an organization")
  void shouldSaveAndFindOrganization() {
    var organization =
        Organization.builder()
            .name("Pool House")
            .contactEmail("mgr@poolhouse.dev")
            .phoneNumber("+1 555 12345")
            .serviceStreetAddress("10 Lake Avenue")
            .serviceCity("Minsk")
            .servicePostalCode("220000")
            .serviceCountry("Belarus")
            .build();
    repository.save(organization);

    assertThat(organization.getId()).as("Organization ID should not be null").isNotNull();

    Optional<Organization> foundOrg = repository.findById(organization.getId());
    assertThat(foundOrg).isEqualTo(Optional.of(organization));
  }
}
