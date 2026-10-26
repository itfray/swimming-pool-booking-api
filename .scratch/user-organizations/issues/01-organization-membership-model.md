# 01: Organization membership model

**What to build:** Add the core Organization domain model and the many-to-many user-to-organization membership relationship needed to represent which users represent which organizations, with the database migration and repository support required for the API to work end to end.

**Blocked by:** None (can start immediately)

**Status:** ready-for-agent

- [ ] Add an Organization entity with the representative-facing profile fields: name, contact information, and service address.
- [ ] Model the user-organization membership as a dedicated join relationship so users can belong to multiple organizations and organizations can have multiple users.
- [ ] Add the persistence and repository layer needed to load organizations and memberships in the authorization flow.
- [ ] Create the migration required to introduce the new table and foreign keys without disturbing the existing user model.
- [ ] Verify the persisted model matches the API access model: representative access is granted via membership, and admin access remains role-based.

