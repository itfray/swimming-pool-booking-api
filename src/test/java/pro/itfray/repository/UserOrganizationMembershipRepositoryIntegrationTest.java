package pro.itfray.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.itfray.domain.Organization;
import pro.itfray.domain.User;
import pro.itfray.domain.UserOrganizationMembership;

class UserOrganizationMembershipRepositoryIntegrationTest extends AbstractDataJpaIntegrationTest {

  @Autowired private UserOrganizationMembershipRepository repository;

  @Autowired private UserRepository userRepository;

  @Autowired private OrganizationRepository organizationRepository;

  @BeforeEach
  void tearDown() {
    repository.deleteAll();
    organizationRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should save and find user-organization memberships")
  void shouldSaveAndFindMemberships() {
    var userId = UUID.randomUUID();

    var user = User.builder().uid(userId).build();
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

    User managedUser = userRepository.saveAndFlush(user);
    Organization managedOrganization = organizationRepository.saveAndFlush(organization);

    var membership = new UserOrganizationMembership(managedUser, managedOrganization);
    managedUser.getMemberships().add(membership);
    managedOrganization.getMemberships().add(membership);

    UserOrganizationMembership savedMembership = repository.saveAndFlush(membership);

    assertThat(managedUser.getId()).as("User ID should not be null").isNotNull();
    assertThat(managedOrganization.getId()).as("Organization ID should not be null").isNotNull();
    assertThat(savedMembership.getId()).as("Membership ID should not be null").isNotNull();

    assertThat(repository.findByUserId(managedUser.getId()))
        .as("Should find membership by user surrogate ID")
        .containsExactly(savedMembership);

    assertThat(repository.findByOrganizationId(managedOrganization.getId()))
        .as("Should find membership by organization ID")
        .containsExactly(savedMembership);

    assertThat(repository.findById(savedMembership.getId()))
        .as("Should find membership by ID")
        .contains(savedMembership);
  }
}
