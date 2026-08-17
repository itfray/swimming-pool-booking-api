package pro.itfray;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Abstract base class for tests using Testcontainers with Singleton Container Pattern. Starts a
 * single PostgreSQL container and a Keycloak container that imports the local realm.
 */
public abstract class AbstractContainerizedTest {

  static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:15-alpine")
      .withInitScript("db/init.sql")
      .withDatabaseName("testdb")
      .withUrlParam("currentSchema", "test")
      .withUsername("testuser")
      .withPassword("testpass");

  static final KeycloakContainer KEYCLOAK = new KeycloakContainer("quay.io/keycloak/keycloak:26.7")
      .withEnv("KC_BOOTSTRAP_ADMIN_USERNAME", "admin")
      .withEnv("KC_BOOTSTRAP_ADMIN_PASSWORD", "admin")
      .withRealmImportFile("keycloak/realm.json");

  static final String REALM = "test";
  static final String CLIENT_ID = "test-api";

  static {
    POSTGRES.start();
    KEYCLOAK.start();
  }

}
