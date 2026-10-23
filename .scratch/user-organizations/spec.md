# User organization membership API

Status: ready-for-agent

## Problem Statement

Authenticated users need a way to view the organizations they represent. Today, the API has no endpoint for retrieving organization records connected to a user, which prevents representatives from seeing the organizations they manage and blocks the organization profile flows required by the product.

The need is especially important for users who act on behalf of multiple organizations. The application must distinguish between:

- a user who is not connected to any organization,
- a user who is connected to one or more organizations, and who is a representative which may only access organizations they are linked to,
- an administrator who may view all organizations.

Without this capability, users cannot access their organization profiles, and future management workflows such as organization registration and approval cannot be built on a reliable access model.

## Solution

Add an organization membership capability to the REST API so that users can retrieve the organizations they represent and open a specific organization profile when they have access.

The solution introduces a first-class Organization domain entity and a user-to-organization membership relationship. Users can be linked to multiple organizations, and each organization may have multiple users linked to it. Access is enforced so that a user sees only their own linked organizations unless they also have the ADMIN role.

The API will expose:

- GET /organizations — returns the current user’s organizations, with optional name filter and sorting
- GET /organizations/{id} — returns the full organization profile for an authorized user

The system will return a 200 with an empty list when the user has no organization memberships, a 404 when the organization does not exist, and a 403 when the organization exists but the current user is not authorized to access it.

## User Stories

1. As an authenticated user, I want to fetch the list of organizations I represent, so that I can quickly see where I have responsibility.
2. As an authenticated user with no linked organizations, I want an empty list response instead of an error, so that the client can handle the state cleanly.
3. As a representative, I want to filter my organizations by name, so that I can find a specific organization faster.
4. As a representative, I want the list to be sorted consistently, so that the API results are predictable and usable in the UI.
5. As an authenticated representative, I want to open a specific organization by id, so that I can view the organization profile relevant to my responsibilities.
6. As a representative, I want access to only organizations I am linked to, so that confidential organization data is not leaked across users.
7. As an administrator, I want to view all organizations, so that I can support operational oversight and management workflows.
8. As an administrator, I want to see organization records that are not linked to the current user, so that I can manage the platform as a whole.
9. As a system owner, I want a dedicated Organization entity, so that organization details can evolve independently of the user profile.
10. As a system owner, I want a user-to-organization relationship modeled explicitly, so that future workflows such as adding members, transferring responsibility, and approval flows are supported.
11. As a developer, I want the access rules to be centralized in the service layer, so that authorization logic is consistent across all organization endpoints.
12. As a security-conscious product owner, I want 403 responses for forbidden organization access, so that clients can distinguish between missing resources and permission failures.
13. As a developer, I want the authentication model to respect Keycloak role mapping, so that ROLE_ADMIN is recognized consistently across the application.
14. As a product owner, I want to support future organization registration flows, so that a user can request to join or create an organization with a clear approval path.
15. As a product owner, I want the API to expose only organization data that is relevant to representatives, so that the profile remains usable and not overly sensitive.
16. As an API consumer, I want the organization response to contain the organization name, contact details, and service address, so that the profile is useful without exposing unrelated system data.
17. As a developer, I want the profile access check to be performed after resource existence validation, so that the API returns the correct status depending on whether the organization exists and whether access is allowed.
18. As an engineering team, I want migration support for the new relationship model, so that the schema change is safe and reproducible.
19. As a platform maintainer, I want tests to cover list and detail access rules, so that regressions in authorization are detected quickly.
20. As a client application, I want a stable API contract for organization retrieval, so that UI development can proceed without hidden assumptions.

## Implementation Decisions

- The user-organization relationship will be modeled as a dedicated join table for membership, instead of embedding authorization directly into the user record or organization record.
- The Organization entity will be introduced as a first-class domain object with an identifier of type Long and the profile fields required for a representative-facing profile: name, contact information, and service address.
- The User entity and Organization entity will be connected through a many-to-many relationship so that a user can represent multiple organizations and an organization can include multiple users.
- Membership is the authorization primitive for this feature: a user is considered authorized to access an organization when the user has a membership to that organization, or when the user has the ADMIN role.
- The GET /organizations endpoint will return only organizations visible to the current authenticated user. This means representatives receive their own organizations, while ADMIN users receive the full organization set.
- The GET /organizations/{id} endpoint will resolve the organization and then enforce authorization. If the organization does not exist, the API returns 404. If the organization exists but is not accessible to the current user, the API returns 403.
- Filtering and sorting will be supported for the organization list endpoint. The minimum expected behavior is filtering by name and sorting the result set in a predictable order.
- Pagination will be supported for the organization list endpoint. The interface is designed around lists that are likely to remain small enough for simple user retrieval but the user with the ADMIN role might need to access larger sets.
- The organization list response should represent all fields available to the authorized caller, without hiding ordinary representative data.
- The future organization registration workflow is not part of this spec, but it will depend on the same user-organization membership model and access control rules.
- The existing Spring Security configuration already prefixes Keycloak roles with ROLE_, so authorization checks using hasRole('ADMIN') are compatible with the current JWT role extraction setup.
- The service layer is the correct enforcement point for organization access. The controller should accept the request, the service should determine authorization and fetch the appropriate records, and the repository should remain focused on persistence behavior.
- The implementation will follow the current application structure: domain model, repository, mapper, DTO, service, and controller layers, with tests at the service and integration levels.

## Testing Decisions

- Tests should validate outward behavior only: the HTTP response status, payload shape, and access rules, not internal implementation details.
- The project already follows a layered test strategy with unit tests for service behavior and integration tests for controller/API flows; this feature should use the same pattern.
- The most important tests are:
  - authenticated user with no organizations receives 200 and an empty list from GET /organizations,
  - authenticated representative receives only their linked organizations,
  - ADMIN receives all organizations,
  - GET /organizations/{id} returns 404 for a non-existent organization,
  - GET /organizations/{id} returns 403 when the organization exists but is not linked to the current user and the user is not ADMIN,
  - GET /organizations/{id} returns 200 when the user has a valid membership,
  - filter by name behavior is correctly applied,
  - sort order is deterministic and matches the contract
  - pagination works as expected.
- Integration tests should exercise the controller endpoints and security context, because the authorization behavior depends on the authenticated user and role mapping.
- A service-level unit test suite should cover the access decision logic separately from the persistence layer to keep the permission model explicit and easy to verify.
- Existing repository and controller test conventions in the project should be reused for naming, assertion style, and endpoint coverage.

## Out of Scope

- Organization creation and editing endpoints
- Organization registration request workflows
- Approval or status management for organization applications
- Admin approval pipeline for organization membership requests
- Organization roles beyond basic representation and administration access
- Advanced search beyond name filtering
- Authorization logic for external third-party providers or non-Keycloak identity systems

## Further Notes

This feature establishes the base access-control and data model required for future organization-management capabilities. The current scope is intentionally narrow: enable representatives to discover and access their own organizations under a clear authorization model, while leaving future registration and membership lifecycle workflows to later specification work.

The design also keeps room for future expansion: if organization membership eventually needs richer metadata such as role, status, or effective dates, the dedicated membership table can evolve without forcing a migration of the core organization model itself.

