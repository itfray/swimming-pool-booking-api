package pro.itfray;

import static java.util.Collections.singletonList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Base class for end-to-end tests that boot the full application.
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractE2eTest extends AbstractSpringIntegrationTest {

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  /**
   * Get a JWT token from Keycloak using username and password.
   *
   * @param username the username
   * @param password the password
   * @return JWT access token
   */
  protected String getToken(String username, String password) {
    final var restTemplate = new RestTemplate();
    final var httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    final var map = new LinkedMultiValueMap<String, String>();
    map.put("grant_type", singletonList("password"));
    map.put("client_id", singletonList(CLIENT_ID));
    map.put("scope", singletonList("openid"));
    map.put("username", singletonList(username));
    map.put("password", singletonList(password));

    String tokeUrl = "%s/realms/%s/protocol/openid-connect/token"
        .formatted(KEYCLOAK.getAuthServerUrl(), REALM);

    final var request = new HttpEntity<>(map, httpHeaders);
    KeyCloakToken token = restTemplate.postForObject(tokeUrl, request, KeyCloakToken.class);

    assert token != null;
    return token.accessToken();
  }

  record KeyCloakToken(@JsonProperty("access_token") String accessToken) {

  }
}
