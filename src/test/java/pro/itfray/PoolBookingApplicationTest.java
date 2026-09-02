package pro.itfray;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Basic integration test for PoolBookingApplication. Verifies that the application context loads
 * correctly and Liquibase migrations are applied.
 */
class PoolBookingApplicationTest extends AbstractE2eTest {

  @Test
  @DisplayName("Should load the Spring context and execute Liquibase migrations successfully")
  void contextLoads() {
    // Test verifies that Spring context loads and Liquibase migrations execute successfully
  }
}
