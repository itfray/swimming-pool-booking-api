# 03: Organization detail API

**What to build:** Expose GET /organizations/{id} so an authorized user can open a specific organization profile, returning 404 when the organization does not exist and 403 when it exists but the current user is not allowed to view it.

**Blocked by:** 01: Organization membership model

**Status:** ready-for-agent

- [ ] Resolve the organization by id before applying access checks so missing resources return 404 instead of 403.
- [ ] Authorize access in the service layer: a user may view the organization if they are linked to it or if they have the ADMIN role.
- [ ] Return a 403 when the organization exists but the caller is not authorized to access it.
- [ ] Return the full organization profile payload for authorized callers, including contact details and service address.
- [ ] Confirm the endpoint behavior matches the security contract for representative and administrator roles.

