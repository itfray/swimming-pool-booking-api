# Swimming Pool Booking API

This backend of web application is designed for companies that provide leisure and recreation services
in poolside relaxation areas. Registered organizations can manage their locations,
recreation areas, and booking availability. Customers can search for nearby recreation areas,
find suitable organizations, and book available spots online.

## Commands
- Build: ./mvnw compile
- Test: ./mvnw test (unit, integration, E2E, needs docker)
- Lint: ./mvnw checkstyle:check (fix errors before pushing)

## Conventions
- Java 21
- Spring Boot 4 as the framework
- Spring Web MVC for REST API
- JPA/Hibernate ORM for database access
- Lombok for boilerplate code
- MapStruct for mapping between layers
- JUnit 5 and AssertJ for tests
- Spring Boot Test for integration tests
- Testcontainers and Rest Assured for E2E tests
- Checkstyle for code style validation according to Google Java Style Guide
- ArchUnit for architecture tests to enforce coding standards and architectural rules
- JoCoCo for code coverage measurement
- Liquibase for database schema versioning and migrations
- Docker for local development environment orchestration
- PostgreSQL 15 is the database, managed by migrations
- Keycloak 26.7 is used for identity and access management. OAuth2 authentication and authorization
- Every endpoint needs an integration test in src/test.

## Architecture

- domain/ holds entities and domain logic
- service/ holds application use cases
- mapper/ holds logic of mapping between dto and domain
- dto/ holds DTOs passed from/to outside
- controller/ holds REST controllers
- repository/ holds logic of interaction with database
- config/ holds application configurations
- docs/ holds project documentation

## Things gets wrong

- Do not bump dependency versions
- Never edit generated classes