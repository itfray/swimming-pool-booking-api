# Swimming Pool Booking API

This backend of web application is designed for companies that provide leisure and recreation services
in poolside relaxation areas. Registered organizations can manage their locations,
recreation areas, and booking availability. Customers can search for nearby recreation areas,
find suitable organizations, and book available spots online.

## Tech stack in use

### Backend

- Java 21 is used as the programming language
- Spring Boot 4 is used for the API
- Data is stored in Postgres, with JPA (Hibernate) as the ORM
  - There are separate database for dev, staging and prod
  - For end to end testing, a new database is created and populated,
    then removed after tests are complete

### Testing

- JUnit 5 for Java
- Spring Boot Test for e2e tests

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