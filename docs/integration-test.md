## Integration Testing with AbstractIntegrationTest

The project includes `AbstractIntegrationTest` for writing integration tests with automatic database setup:

### Using AbstractIntegrationTest

```java
class MyFeatureTest extends AbstractIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void testUserCreation() {
    User user = new User();
    user.setEmail("test@example.com");

    User saved = userRepository.save(user);

    assertNotNull(saved.getId());
  }
}
```

### How It Works

1. **Singleton Container Pattern** - One PostgreSQL Docker container is started per test run and reused across all tests for performance
2. **Static Initialization** - The container starts before any tests run via static block
3. **Dynamic Properties** - `@DynamicPropertySource` method injects container connection details into Spring configuration
4. **Automatic Migrations** - Liquibase migrations execute automatically during Spring context startup
5. **Clean State** - Each test sees a fresh database with all migrations applied

### Key Points

- Tests use actual database connections through TestContainers
- All Liquibase migrations (base + test-specific) are applied before tests run
- No mocking of database access - true integration tests
- Slower than unit tests but provide realistic scenarios
- Container is stopped after all tests complete