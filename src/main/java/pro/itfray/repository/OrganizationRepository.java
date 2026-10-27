package pro.itfray.repository;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.itfray.domain.Organization;

/** Repository for Organization entities. */
@NullMarked
public interface OrganizationRepository extends JpaRepository<Organization, Long> {}
