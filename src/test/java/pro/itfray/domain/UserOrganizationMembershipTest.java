package pro.itfray.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserOrganizationMembershipTest {

  @Test
  @DisplayName("Should create membership with user and organization")
  void shouldCreateMembershipWithUserAndOrganization() {
    User user = user(UUID.randomUUID());
    Organization organization = organization(1L, "Pool House");

    UserOrganizationMembership membership = new UserOrganizationMembership(user, organization);

    assertThat(membership.getUser()).isEqualTo(user);
    assertThat(membership.getOrganization()).isEqualTo(organization);
  }

  @Test
  @DisplayName("Should return true when comparing same object instance")
  void shouldReturnTrueWhenSameInstance() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(1L, "Pool House"));

    // noinspection EqualsWithItself
    assertThat(membership).isEqualTo(membership);
  }

  @Test
  @DisplayName("Should return false when comparing membership with null")
  void shouldReturnFalseWhenComparedWithNull() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(1L, "Pool House"));

    assertThat(membership).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return false when comparing with different class")
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(1L, "Pool House"));

    // noinspection AssertBetweenInconvertibleTypes
    assertThat(membership).isNotEqualTo("not a membership");
  }

  @Test
  @DisplayName("Should return true when memberships have same id")
  void shouldReturnTrueWhenIdsAreEqual() {
    User user = user(UUID.randomUUID());
    Organization organization = organization(1L, "Pool House");
    UserOrganizationMembership membership1 = new UserOrganizationMembership(user, organization);
    membership1.setId(10L);
    UserOrganizationMembership membership2 = new UserOrganizationMembership(user, organization);
    membership2.setId(10L);

    assertThat(membership1).isEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return false when memberships have different ids")
  void shouldReturnFalseWhenIdsAreDifferent() {
    User user = user(UUID.randomUUID());
    Organization organization = organization(1L, "Pool House 1");
    UserOrganizationMembership membership1 = new UserOrganizationMembership(user, organization);
    membership1.setId(10L);
    UserOrganizationMembership membership2 = new UserOrganizationMembership(user, organization);
    membership2.setId(11L);

    assertThat(membership1).isNotEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return false when membership id is null")
  void shouldReturnFalseWhenMembershipIdIsNull() {
    UserOrganizationMembership membership1 =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(1L, "Pool House"));
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(2L, "Pool House"));

    assertThat(membership1).isNotEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return same hashCode for memberships of same class")
  void shouldReturnSameHashCodeForSameClass() {
    UserOrganizationMembership membership1 =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(1L, "Pool House"));
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(user(UUID.randomUUID()), organization(2L, "Pool House"));

    assertThat(membership1).hasSameHashCodeAs(membership2);
  }

  private User user(UUID uid) {
    return User.builder().uid(uid).theme(Theme.WHITE).build();
  }

  private Organization organization(Long id, String name) {
    return Organization.builder()
        .id(id)
        .name(name)
        .contactEmail("mgr@poolhouse.dev")
        .phoneNumber("+1 555 12345")
        .serviceStreetAddress("10 Lake Avenue")
        .serviceCity("Minsk")
        .servicePostalCode("220000")
        .serviceCountry("Belarus")
        .build();
  }
}
