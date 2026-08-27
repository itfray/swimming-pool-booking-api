# Swimming Pool Booking API

This backend of web application is designed for companies that provide leisure and recreation services
in poolside relaxation areas. Registered organizations can manage their locations,
recreation areas, and booking availability. Customers can search for nearby recreation areas,
find suitable organizations, and book available spots online.

## Tech stack in use

### Backend

- **Java 21** is used as the programming language
- **Spring Boot 4** is used as the framework with the following starters:
  - `spring-boot-starter-web` for REST API
  - `spring-boot-starter-actuator` for health checks and monitoring
  - `spring-boot-starter-data-jpa` for database access (JPA/Hibernate ORM)
  - `spring-boot-starter-validation` for input validation
  - `spring-boot-starter-security` for authentication and authorization
  - `spring-boot-starter-oauth2-resource-server` for OAuth2 resource server configuration
  - `spring-boot-starter-liquibase` for database schema versioning and migrations
  - `spring-boot-devtools` for development experience improvements
- **PostgreSQL** is the database, managed by Liquibase for migrations
  - Separate databases for dev, staging, and prod environments
  - For end-to-end testing, a new database is created via Testcontainers,
    populated during tests, then removed automatically after tests complete
- **Keycloak 26.7** is used for identity and access management (IAM)
  - Deployed via Docker Compose alongside PostgreSQL
  - Provides OAuth2/OIDC authentication and authorization
- **Docker Compose** for local development environment orchestration
  - PostgreSQL 15-alpine container
  - Keycloak container with realm import configuration

### Testing

- **JUnit 5** for Java unit tests
- **AssertJ** for fluent assertion statements
- **Spring Boot Test** for integration and e2e tests
- **Testcontainers** with PostgreSQL and Keycloak modules
  - PostgreSQL container is provisioned and managed automatically for isolated database tests
  - Keycloak container is provisioned and managed automatically for e2e tests with authentication and authorization

## Project and code guidelines

- Always use type hints in any language which supports them
- Unit tests are required, and are required to pass before PR
  - Unit tests should focus on core functionality
- End-to-end tests are required
  - End-to-end tests should focus on core functionality
  - End-to-end tests should validate accessibility
- Always follow good security practices
- Follow RESTful API design principles
- Use scripts to perform actions when available

## Project structure

- `src/main/java`: Java backend code
- `src/test/java`: Unit tests and integration tests
- `src/main/resources`: Files to be available in the classpath of the main code
- `src/test/resources`: Files to be available in the classpath of the test code
- `scripts/` : Development, deployment and testing scripts
- `docs/` : Project documentation to be kept in sync at all times