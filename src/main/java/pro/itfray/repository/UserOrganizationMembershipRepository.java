package pro.itfray.repository;

import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.itfray.domain.UserOrganizationMembership;

/** Repository managing membership relations between users and organizations. */
@NullMarked
public interface UserOrganizationMembershipRepository
    extends JpaRepository<UserOrganizationMembership, Long> {

  /**
   * Find all memberships for the given user's surrogate ID.
   *
   * @param userId user identifier
   * @return memberships belonging to the user
   */
  @Query("select m from UserOrganizationMembership m where m.user.id = :userId")
  List<UserOrganizationMembership> findByUserId(@Param("userId") Long userId);

  /**
   * Find all memberships for the given organization.
   *
   * @param organizationId organization identifier
   * @return memberships belonging to the organization
   */
  @Query("select m from UserOrganizationMembership m where m.organization.id = :organizationId")
  List<UserOrganizationMembership> findByOrganizationId(
      @Param("organizationId") Long organizationId);
}
