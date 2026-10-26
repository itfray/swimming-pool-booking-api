# 02: User organization list API

**What to build:** Expose GET /organizations so an authenticated user sees the organizations they represent, with filtering, sorting, and pagination that work consistently for representatives and administrators.

**Blocked by:** 01: Organization membership model

**Status:** ready-for-agent

- [ ] Return the current authenticated user’s visible organizations from the list endpoint, including the empty-list case for users with no memberships.
- [ ] Support name-based filtering and predictable ordering in the list result set.
- [ ] Add pagination to the list response so the API remains usable as the organization set grows.
- [ ] Return only the organization fields appropriate to a representative-facing profile, without exposing unrelated sensitive data.
- [ ] Ensure ADMIN users can access the full organization set while standard representatives only see their own linked organizations.

