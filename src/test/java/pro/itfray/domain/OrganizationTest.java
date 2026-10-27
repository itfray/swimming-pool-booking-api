package pro.itfray.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrganizationTest {

  @Test
  @DisplayName("Should return true when comparing same object instance")
  void shouldReturnTrueWhenSameInstance() {
    long orgId = 1L;
    Organization organization = organization(orgId, "Pool House");

    // noinspection EqualsWithItself
    assertThat(organization).isEqualTo(organization);
  }

  @Test
  @DisplayName("Should return true when comparing organizations with same id")
  void shouldReturnTrueWhenIdsAreEqual() {
    Organization organization1 = organization(1L, "Pool House");
    Organization organization2 = organization(1L, "Other Name");

    assertThat(organization1).isEqualTo(organization2);
  }

  @Test
  @DisplayName("Should return false when comparing organizations with different ids")
  void shouldReturnFalseWhenIdsAreDifferent() {
    Organization organization1 = organization(1L, "Pool House");
    Organization organization2 = organization(2L, "Pool House");

    assertThat(organization1).isNotEqualTo(organization2);
  }

  @Test
  @DisplayName("Should return false when comparing organization with null")
  void shouldReturnFalseWhenComparedWithNull() {
    Organization organization = organization(1L, "Pool House");

    assertThat(organization).isNotEqualTo(null);
  }

  @Test
  @DisplayName("Should return false when comparing with different class")
  void shouldReturnFalseWhenComparedWithDifferentClass() {
    Organization organization = organization(1L, "Pool House");

    // noinspection AssertBetweenInconvertibleTypes
    assertThat(organization).isNotEqualTo("not an organization");
  }

  @Test
  @DisplayName("Should return false when organization id is null")
  void shouldReturnFalseWhenOrganizationIdIsNull() {
    Organization organization1 = organization(null, "Pool House");
    Organization organization2 = organization(2L, "Pool House");

    assertThat(organization1).isNotEqualTo(organization2);
  }

  @Test
  @DisplayName("Should return same hashCode for organizations of same class")
  void shouldReturnSameHashCodeForSameClass() {
    Organization organization1 = organization(1L, "Pool House");
    Organization organization2 = organization(2L, "Other Name");

    assertThat(organization1).hasSameHashCodeAs(organization2);
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
