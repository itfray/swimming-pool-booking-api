## Integration testing — test base classes

This project provides a small hierarchy of test base classes used by different kinds of integration tests. They centralize Testcontainers setup, Spring property wiring, and reusable helpers.

### Test base classes

- AbstractContainerizedTest
  - Starts shared Testcontainers used by tests (PostgreSQL and Keycloak) using a singleton pattern.
  - The Keycloak container imports the local realm configuration (keycloak/realm.json).
  - Containers are started in a static block and are reused across test classes for performance.

- AbstractSpringIntegrationTest
  - Extends AbstractContainerizedTest.
  - Uses @DynamicPropertySource to inject container connection details into Spring properties (datasource URL/username/password and Keycloak JWK URI).
  - Activates the "test" profile in participating tests.

- AbstractE2ETest
  - Base for end-to-end (full application) tests annotated with @SpringBootTest(webEnvironment = RANDOM_PORT).
  - Sets RestAssured.port to the random port and provides helper methods (e.g., getToken(username,password)) to obtain Keycloak tokens for test requests.

- AbstractDataJpaIntegrationTest
  - @DataJpaTest slice for JPA repository integration tests. Extends AbstractSpringIntegrationTest so Testcontainers properties are applied.
  - Use this for repository-level tests that need a real database.

- AbstractWebMvcTest
  - @WebMvcTest slice for controller contract tests. Imports SecurityConfig so controller security configuration is available.
  - Use this for fast controller tests (MockMvc / controller slice) and not full Spring context startup.

### Where test properties live

- src/test/resources/application-test.yaml contains default test properties (datasource pointing to localhost and a default JWKS URI). AbstractSpringIntegrationTest overrides these at runtime using container values via @DynamicPropertySource.

### Examples

Repository integration test (real DB):
```java
public class UserRepositoryIntegrationTest extends AbstractDataJpaIntegrationTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldSaveUser() {
    var u = new User();
    u.setEmail("a@b.com");
    var saved = userRepository.save(u);
    assertNotNull(saved.getId());
  }
}
```

End-to-end test (running app + Keycloak):
```java
public class UserControllerE2ETest extends AbstractE2ETest {
  @Test
  void shouldReturnProfileWhenAuthenticated() {
    String token = getToken("user","password");
    RestAssured.given()
        .header("Authorization", "Bearer " + token)
        .when()
        .get("/api/profile")
        .then()
        .statusCode(200);
  }
}
```

Web MVC controller slice (fast, no containers required):
```java
public class UserControllerTest extends AbstractWebMvcTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnOk() throws Exception {
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk());
  }
}
```

### Key points and tradeoffs

- Containerized tests (AbstractSpringIntegrationTest / AbstractE2ETest / AbstractDataJpaIntegrationTest) use real PostgreSQL and Keycloak instances — they are slower but provide realistic coverage and execute Liquibase migrations automatically.
- WebMvc slice tests (AbstractWebMvcTest) are much faster and suitable for controller-level contract tests where full application startup is unnecessary.
- Testcontainers are started once per JVM run (singleton) and are stopped when the JVM exits.
