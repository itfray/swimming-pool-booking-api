# 04: Access-control regression tests

**What to build:** Cover the list and detail organization flows with service and integration tests that validate access rules, empty-list behavior, filtering, sorting, pagination, and the correct HTTP status codes under representative and administrator roles.

**Blocked by:** 02: User organization list API, 03: Organization detail API

**Status:** ready-for-agent

- [ ] Add service tests covering the authorization decisions for users with no memberships, representative access, and ADMIN access.
- [ ] Add integration tests for GET /organizations covering empty results, name filtering, sorting, pagination, and role-based visibility.
- [ ] Add integration tests for GET /organizations/{id} covering 404 for missing organizations, 403 for forbidden access, and 200 for authorized access.
- [ ] Verify the auth model respects Keycloak role mapping so ROLE_ADMIN is recognized by the application security configuration.
- [ ] Ensure the API contract remains stable and regression-safe for future organization-management workflows.

