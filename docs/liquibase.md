# Liquibase Database Migration Guide

## Overview

This project uses Liquibase for database schema versioning and migration management. Liquibase replaces Hibernate's auto-schema generation, providing better control over database changes, version tracking, and environment-specific configurations.

## Project Structure

Migrations are organized in a version-based directory structure that aligns with application versioning:

```
src/main/resources/db/changelog/
├── base/                    # Common migrations for all environments
│   └── v1/                  # Version 1 migrations
│       ├── v1_1_initial_schema.xml
│       ├── v1_2_create_users_table.xml
│       └── ...
├── prod/                    # Production-specific migrations
│   └── v1/
│       ├── v1_1_prod_init.xml
│       └── ...
├── dev/                     # Development-specific migrations
│   └── v1/
│       ├── v1_1_dev_init.xml
│       └── ...
├── test/                    # Test-specific migrations
│   └── v1/
│       ├── v1_1_test_init.xml
│       └── ...
├── db.changelog-prod.xml    # Master changelog for production
├── db.changelog-dev.xml     # Master changelog for development
└── db.changelog-test.xml    # Master changelog for tests
```

### Naming Convention

Each migration file follows this pattern:

```
{version}_{sequence_number}_{description}.xml
```

**Examples:**
- `v1_1_initial_schema.xml` - First migration for version 1
- `v1_2_create_users_table.xml` - Second migration for version 1
- `v2_1_add_roles_table.xml` - First migration for version 2
- `v2_2_create_permissions_table.xml` - Second migration for version 2

### Migration Categories

**base/** - Common migrations executed in all environments
- Schema creation (tables, columns, constraints)
- Shared data structures used across all environments

**prod/** - Production environment only
- Production-specific initialization
- Security-related setup
- Performance tuning indexes

**dev/** - Development environment only
- Sample data for development and testing
- Development-only tables or features
- Looser constraints for easier local testing

**test/** - Test environment only
- Test-specific initialization
- TestContainers setup
- Seed data for integration tests

## Configuration by Environment

### Production (application.yaml)

```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-prod.xml
  jpa:
    hibernate:
      ddl-auto: none
```

### Development (application-dev.yaml)

```yaml
spring:
  liquibase:
    change-log: classpath:db/changelog/db.changelog-dev.xml
  jpa:
    hibernate:
      ddl-auto: validate
```

### Testing (application-test.yaml)

```yaml
spring:
  liquibase:
    change-log: classpath:db/changelog/db.changelog-test.xml
  jpa:
    hibernate:
      ddl-auto: validate
```

**Important:** `ddl-auto: none` is required for Production environment when using Liquibase. Hibernate will not auto-generate the schema.

## Creating Migrations

### Step 1: Determine the Version

Check the current version in `pom.xml` or the latest version directory in `db/changelog/`:
- If releasing version 2.0, use `v2` directory
- If still on version 1.x, use `v1` directory

### Step 2: Find the Next Sequence Number

Look at existing migrations in the target version directory:
- If `v1_1_*.xml` exists, the next is `v1_2_*.xml`
- If `v1_5_*.xml` is the latest, the next is `v1_6_*.xml`

### Step 3: Create the Migration File

Create the file in the appropriate directory:

```
src/main/resources/db/changelog/{base|prod|dev|test}/v{X}/v{X}_{Y}_{description}.xml
```

Choose the correct subdirectory:
- **base/** for common migrations
- **prod/** for production-only changes
- **dev/** for development-only changes
- **test/** for test-only changes

## Migration Templates

### Basic Table Creation

```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
  xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
    http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

  <changeSet id="v1_2_create_users_table" author="team">
    <comment>Create users table</comment>
    
    <createTable tableName="users">
      <column name="id" type="BIGINT">
        <constraints primaryKey="true" nullable="false"/>
      </column>
      <column name="email" type="VARCHAR(255)">
        <constraints nullable="false" unique="true"/>
      </column>
      <column name="password" type="VARCHAR(255)">
        <constraints nullable="false"/>
      </column>
      <column name="created_at" type="TIMESTAMP">
        <constraints nullable="false" defaultValue="CURRENT_TIMESTAMP"/>
      </column>
      <column name="updated_at" type="TIMESTAMP">
        <constraints nullable="false" defaultValue="CURRENT_TIMESTAMP"/>
      </column>
    </createTable>
  </changeSet>

</databaseChangeLog>
```

### Adding a Column

```xml
<changeSet id="v1_3_add_phone_to_users" author="team">
  <comment>Add phone column to users table</comment>
  
  <addColumn tableName="users">
    <column name="phone" type="VARCHAR(20)"/>
  </addColumn>
</changeSet>
```

### Creating an Index

```xml
<changeSet id="v1_4_create_users_email_index" author="team">
  <comment>Create index on users.email</comment>
  
  <createIndex tableName="users" indexName="idx_users_email">
    <column name="email"/>
  </createIndex>
</changeSet>
```

### Adding a Foreign Key

```xml
<changeSet id="v1_5_add_user_id_to_bookings" author="team">
  <comment>Add foreign key from bookings to users</comment>
  
  <addForeignKeyConstraint
    baseTableName="bookings"
    baseColumnNames="user_id"
    referencedTableName="users"
    referencedColumnNames="id"
    constraintName="fk_bookings_user_id"/>
</changeSet>
```

### Insert Test Data

```xml
<changeSet id="v1_1_dev_init" author="team">
  <comment>Insert sample data for development</comment>
  
  <insert tableName="users">
    <column name="id" value="1"/>
    <column name="email" value="admin@example.com"/>
    <column name="password" value="$2a$10$...encrypted_password..."/>
    <column name="created_at" value="2024-01-01T00:00:00Z"/>
    <column name="updated_at" value="2024-01-01T00:00:00Z"/>
  </insert>
</changeSet>
```

## Running Migrations

### Development Environment

```bash
# Start with dev profile - applies base + dev migrations
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Production Environment

```bash
# Start with production profile - applies base + prod migrations
java -jar target/swimming-pool-booking-api-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### Testing

```bash
# Run integration tests - applies base + test migrations via TestContainers
./mvnw test
```

## Best Practices

1. **Keep changesets small and focused** - One logical change per changeset
2. **Use meaningful changeset IDs** - Include version, sequence, and description
3. **Always include comments** - Explain what each migration does
4. **Test migrations locally first** - Verify against local PostgreSQL
5. **Use `logicalFilePath`** - For refactored files to maintain changelog integrity
6. **Avoid breaking changes in production** - Use backwards-compatible migrations
7. **Version migrations with application** - New version = new v{X} directory

## Troubleshooting

### Migration Failed

Check the `databasechangelog` table:
```sql
SELECT * FROM databasechangelog WHERE id LIKE '%v1_2%';
```

### Rollback a Migration

In Liquibase, use `rollback` changesets:
```xml
<changeSet id="v1_2_revert" author="team">
  <comment>Rollback previous changes</comment>
  <rollback>
    <dropTable tableName="temp_table"/>
  </rollback>
</changeSet>
```

### Force Execution

If a changeset was modified (not recommended), you can force re-execution:
```xml
<changeSet id="v1_2_fixed" author="team" runAlways="true">
  <comment>This runs every time</comment>
</changeSet>
```

## References

- [Liquibase Official Documentation](https://docs.liquibase.com/)
- [Spring Boot Liquibase Integration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.liquibase)
- [PostgreSQL Data Types](https://www.postgresql.org/docs/current/datatype.html)
- [TestContainers Documentation](https://testcontainers.com/)
