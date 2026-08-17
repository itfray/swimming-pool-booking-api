package pro.itfray;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public class AbstractSpringIntegrationTest extends AbstractContainerizedTest {

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () ->
        "%s/realms/%s/protocol/openid-connect/certs".formatted(KEYCLOAK.getAuthServerUrl(), REALM));
  }
}
