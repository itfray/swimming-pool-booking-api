# Swimming Pool Booking API

This backend of web application is designed for companies that provide leisure and recreation services
in poolside relaxation areas. Registered organizations can manage their locations,
recreation areas, and booking availability. Customers can search for nearby recreation areas,
find suitable organizations, and book available spots online.

## Tech stack in use

### Backend

- **Java 21** is used as the programming language
- **Spring Boot 4** is used for the API with the following starters:
  - `spring-boot-starter-web` for REST API
  - `spring-boot-starter-data-jpa` for database access (JPA/Hibernate ORM)
  - `spring-boot-starter-validation` for input validation
  - `spring-boot-starter-liquibase` for database schema versioning and migrations
- **PostgreSQL** is the database, managed by Liquibase for migrations
  - Separate databases for dev, staging, and prod environments
  - For end-to-end testing, a new database is created via Testcontainers,
    populated during tests, then removed automatically after tests complete

### Testing

- **JUnit 5** for Java unit tests
- **Spring Boot Test** for integration and e2e tests
- **Testcontainers** with PostgreSQL module for isolated database testing
  - PostgreSQL container is provisioned and managed automatically for tests
  - No manual database setup required for test execution

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