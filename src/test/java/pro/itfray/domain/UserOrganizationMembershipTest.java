package pro.itfray.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserOrganizationMembershipTest {

  @Test
  @DisplayName("Should create membership with user and organization")
  void shouldCreateMembershipWithUserAndOrganization() {
    User user = user(1L, UUID.randomUUID());
    Organization organization = organization(1L, "Pool House");

    UserOrganizationMembership membership = new UserOrganizationMembership(user, organization);

    assertThat(membership.getUser()).isEqualTo(user);
    assertThat(membership.getOrganization()).isEqualTo(organization);
    assertThat(membership.getId()).isEqualTo(new UserOrganizationMembershipId(1L, 1L));
  }

  @Test
  @DisplayName("Should return true when comparing same object instance")
  void shouldReturnTrueWhenSameInstance() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(1L, UUID.randomUUID()), organization(1L, "Pool House"));

    // noinspection EqualsWithItself
    assertThat(membership).isEqualTo(membership);
  }

  @Test
  @DisplayName("Should return false when comparing membership with null")
  void shouldReturnFalseWhenComparedWithNull() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(1L, UUID.randomUUID()), organization(1L, "Pool House"));

    assertThat(membership).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return false when comparing with different class")
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(user(1L, UUID.randomUUID()), organization(1L, "Pool House"));

    // noinspection AssertBetweenInconvertibleTypes
    assertThat(membership).isNotEqualTo("not a membership");
  }

  @Test
  @DisplayName("Should return true when memberships have the same user and organization IDs")
  void shouldReturnTrueWhenCompositeIdsAreEqual() {
    User user = user(10L, UUID.randomUUID());
    Organization organization = organization(1L, "Pool House");
    UserOrganizationMembership membership1 = new UserOrganizationMembership(user, organization);
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Other name"));

    assertThat(membership1).isEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return false when memberships have different user IDs")
  void shouldReturnFalseWhenUserIdsAreDifferent() {
    UserOrganizationMembership membership1 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Pool House"));
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(
            user(11L, UUID.randomUUID()), organization(1L, "Pool House"));

    assertThat(membership1).isNotEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return false when memberships have different organization IDs")
  void shouldReturnFalseWhenOrganizationIdsAreDifferent() {
    UserOrganizationMembership membership1 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Pool House"));
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(2L, "Other Pool House"));

    assertThat(membership1).isNotEqualTo(membership2);
  }

  @Test
  @DisplayName("Should return true for hashCode consistency")
  void shouldReturnConsistentHashCode() {
    UserOrganizationMembership membership =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Pool House"));

    assertThat(membership).hasSameHashCodeAs(membership);
  }

  @Test
  @DisplayName("Should use the same hashCode for equal composite IDs")
  void shouldReturnSameHashCodeForSameCompositeId() {
    UserOrganizationMembership membership1 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Pool House"));
    UserOrganizationMembership membership2 =
        new UserOrganizationMembership(
            user(10L, UUID.randomUUID()), organization(1L, "Other name"));

    assertThat(membership1).hasSameHashCodeAs(membership2);
  }

  private User user(Long id, UUID uid) {
    return User.builder().id(id).uid(uid).theme(Theme.WHITE).build();
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
