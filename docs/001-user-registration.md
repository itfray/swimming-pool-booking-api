# User Registration in the Application

## 1. Purpose of the Functionality

The functionality is intended to create and retrieve a local application user profile after the user has successfully registered in Keycloak.

The system uses two sources of user data:

* **Keycloak** — Identity Provider (IdP), and the source of data about the user's identity and account:

  * `uid`;
  * `username`;
  * `email`;
  * `firstName`;
  * `lastName`.
* **PostgreSQL** — and the source of data for the application:

  * user's profile;
  * theme of the interface `theme`;
  * information about the presence of related organizations.

Keycloak is used for OAuth2/OIDC authentication and authorization of the application.

The local `user` table must not duplicate passwords, credentials, or other authentication-related data from Keycloak.

---

## 2. User Flow

#### Main Happy Path

```text
User
    |
    | 1. Opens the application
    v
Frontend
    |
    | 2. Redirects to Keycloak
    v
Keycloak
    |
    | 3. User registration
    |    email / username / first name / last name / password
    |
    | 4. Authentication
    v
Frontend
    |
    | 5. Receives OAuth2/OIDC token
    |
    | 6. POST /v1/users request
    v
Backend
    |
    | 7. Validates JWT
    | 8. Extracts uid from token
    | 9. Checks whether the user exists in PostgreSQL
    |
    +---- user does not exist ----+
    |                                   |
    |                                   v
    |                             INSERT user
    |
    +-----------------------------------+
    |
    | 10. GET /v1/users/{id}
    v
Backend
    |
    +---- JWT: Keycloak identity data
    |
    +---- PostgreSQL: application data
    |
    v
Frontend
    |
    | 11. Displays the profile
    |
    | 12. User is navigated
    |     to search for recreation areas
    v
Search for recreation areas
```

---

## 3. System Responsibility Boundaries

| Data                           | Source System                |
|--------------------------------|------------------------------|
| User UID                       | Keycloak                     |
| Username                       | Keycloak                     |
| Email                          | Keycloak                     |
| First name                     | Keycloak                     |
| Last name                      | Keycloak                     |
| Theme                          | PostgreSQL                   |
| Organization relationships     | PostgreSQL                   |
| Authentication                 | Keycloak                     |
| Authorization                  | Keycloak + backend security  |
| Local application profile      | PostgreSQL                   |

Thus, **Keycloak is the master source for identity attributes**, while PostgreSQL is the master source for application-specific attributes.

This is important because the backend must not accept `uid`, `email`, `username`, or similar identity fields from the frontend in `POST /v1/users`. They must be determined from the authenticated user context.

---

## 4 Authentication

Endpoints must be secured.

```http
Authorization: Bearer <access_token>
```

The backend must:

1. check for the presence of a Bearer token;
2. verify the JWT signature;
3. verify the issuer;
4. verify the token expiration;
5. obtain the user's UID from the JWT claims.

The standard `sub` claim is used to store the UID, and this must be fixed in the Keycloak integration configuration.

---

## 5 Endpoint Description

### 5.1 POST `/v1/users`

#### 5.1.1 Purpose

The endpoint is intended for **creating a local application user after successful registration in Keycloak**.

The endpoint does not register the user in Keycloak.

Account registration is performed by Keycloak.

`POST /v1/users` creates the local user profile in PostgreSQL.

---

#### 5.1.2 Request Structure

Identity attributes do not need to be passed in the request body.

```http
POST /v1/users
Authorization: Bearer <access_token>
Content-Type: application/json
```

The backend must trust identity only from the authenticated security context.

---

#### 5.1.3 Response Structure

For successful creation:

```http
201 Created
```

```json
{
  "uid": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john.doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "theme": "WHITE"
}
```

POST is used as an **idempotent provisioning endpoint**,
and a repeated request may find an existing user.

---

#### 5.1.4 HTTP Response Codes

| Situation              |                      Status |
|------------------------|----------------------------:|
| User created           |               `201 Created` |
| User already exists    |               `201 Created` |
| Invalid/missing token  |          `401 Unauthorized` |
| Invalid request        |           `400 Bad Request` |
| DB error               | `500 Internal Server Error` |

---

#### 5.1.5 Algorithm

```text
1. Obtain the authenticated principal
2. Obtain the Keycloak user UID
3. Check whether the user exists in PostgreSQL
4. If the user exists:
       return the existing profile
5. If the user does not exist:
       create the user
       set the default theme
       save the user
6. Return the created user
```

The endpoint must be **idempotent by UID**.

That is, a repeated call:

```http
POST /v1/users
```

for the same Keycloak UID must not create a second user.

Concurrent requests must be handled.

For example, the frontend accidentally sends two requests simultaneously:

```text
Request A ---> POST /v1/users
Request B ---> POST /v1/users
```

Both check:

```text
user doesn't exist
```

and attempt to execute:

```sql
INSERT
```

Solution:

1. `uid` must be the `PRIMARY KEY`;
2. user creation must correctly handle a unique constraint violation.

Thus, PostgreSQL guarantees that there cannot be two records for the same user.

---

#### 5.1.6 Sequence Diagram

```text
Frontend
   |
   | POST /v1/users + JWT
   v
Backend
   |
   | validate JWT
   |
   | extract uid
   |
   v
PostgreSQL
   |
   | SELECT user WHERE uid = ?
   |
   +---- exists ----> return existing profile
   |
   +---- not exists
           |
           v
      create user
      theme = default
           |
           v
      PostgreSQL
           |
           v
      return profile
```
---

### 5.2 GET `/v1/users/{id}`

#### 5.2.1 Purpose

The endpoint returns an aggregated user profile.

The response is built from two sources:

```text
Keycloak
    +
PostgreSQL
    =
User Profile
```

```http
GET /v1/users/{id}
Authorization: Bearer <access_token>
```

Sample:

```http
GET /users/550e8400-e29b-41d4-a716-446655440000
```

where `{id}` is the user's UID.

---

#### 5.2.2 Response Structure

```json
{
  "uid": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john.doe",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "hasOrganization": true,
  "theme": "WHITE"
}
```

where

| Field             | Type    | Source     | Required |
|-------------------|---------|------------|----------|
| `uid`             | String  | Keycloak   | Yes      |
| `username`        | String  | Keycloak   | Yes      |
| `email`           | String  | Keycloak   | Yes      |
| `firstName`       | String  | Keycloak   | No*      |
| `lastName`        | String  | Keycloak   | No*      |
| `hasOrganization` | Boolean | PostgreSQL | Yes      |
| `theme`           | Enum    | PostgreSQL | Yes      |

> actual requiredness depends on the Keycloak registration settings.

---

#### 5.2.3 HTTP Response Codes

| Situation                        |                      Status |
|----------------------------------|----------------------------:|
| Success                          |                    `200 OK` |
| Not Found                        |             `404 Not Found` |
| Invalid/missing token            |          `401 Unauthorized` |
| Access to another user's profile |             `403 Forbidden` |
| DB error                         | `500 Internal Server Error` |

error response for authorization:

```json
{
  "code": "ACCESS_DENIED",
  "message": "Access to this user profile is denied",
  "timestamp": "2026-08-14T19:00:00Z"
}
```

---

#### 5.2.4 Authorization

Authorization rule - authenticated user can access only own profile.

That is:

```text
JWT.sub == path.id
```

If:

```text
JWT.sub = user-A
GET /users/user-B
```

the backend must return:

```http
403 Forbidden
```

---

#### 5.2.5 Algorithm

```text
1. Authenticate request
2. Extract authenticated UID
3. Validate access to requested {id}
4. Get the user from PostgreSQL
5. Get identity data from JWT claims
6. Get information about organization relationships
7. Calculate has_organization
8. Combine the data
9. Return the response
```

> `has_organization` is a flag indicating whether the user is linked to 0..n organizations.

This means that the user may have:

```text
0 organizations -> false
1 organization -> true
N organizations -> true
```

Therefore, the backend **must not return the number of organizations instead of a boolean**.

Logic:

```sql
EXISTS (
    SELECT 1
    FROM user_organization
    WHERE user_uid = :uid
)
```

Result:

```text
0 -> false
>0 -> true
```

To obtain some of the data from Keycloak, JWT claims must be used

```text
JWT
 |
 +-- username
 +-- email
 +-- firstName
 +-- lastName
```

The response is therefore built using the following scheme:

```text
GET /v1/users/{id}
       |
       +--> JWT claims
       |
       +--> PostgreSQL
```

#### 5.2.6 Sequence Diagram

```text
Frontend
   |
   | GET /users/{uid}
   v
Backend
   |
   | Validate JWT
   |
   | JWT.sub == {uid} ?
   |
   +---- NO ---> 403
   |
   +---- YES
          |
          +--> PostgreSQL
          |       |
          |       +--> theme
          |       +--> hasOrganization
          |
          +--> JWT
                  |
                  +--> username
                  +--> email
                  +--> firstName
                  +--> lastName
          |
          v
       UserResponse
```

---

## 6 Database Data Model

### 6.1 User Model

```text
user
--------------------------------
uid       PK
theme
```

#### `uid`

* required;
* unique;
* user identifier;
* matches the Keycloak User UID.

#### `theme`

Allowed values:

```text
BLACK
WHITE
```

When the user is first created, the default value `WHITE` must be set.

---

### 6.2 Organization Model

Although the organization table is not described in the current requirement, a relationship between the user and organizations must exist to implement `hasOrganization`.

For example:
```text
user
----------------
uid
theme

organization
----------------
id
...

user_organization
----------------
user_uid
organization_id
```

Relationship:
```text
User 1 ──────── N UserOrganization N ──────── 1 Organization
```

This makes it possible to represent:
```text
User A -> 0 organizations
User B -> 1 organization
User C -> N organizations
```

For this API, `EXISTS` is sufficient; there is no need to load all organizations.

---

## 7. Test Scenarios

#### 7.1 POST `/users`

##### **TC-01 — Create a New User**

```text
Given valid Keycloak JWT
And user does not exist in PostgreSQL

When POST /v1/users

Then 201 Created
And user exists in PostgreSQL
And uid = JWT user uid
And theme = default theme
```

##### **TC-02 — Repeat Creation**

```text
Given user already exists

When POST /v1/users

Then no duplicate user is created
```

##### **TC-03 — missing/invalid token**

```text
When POST /v1/users without Authorization or with invalid JWT

Then 401 Unauthorized
```

##### **TC-04 — concurrent provisioning**

```text
Given user does not exist

When two POST /v1/users requests arrive concurrently

Then exactly one user exists
```

---

#### 7.2 GET `/v1/users/{id}`

##### **TC-01 — Get Own Profile**

```text
Given authenticated user A

When GET /users/A

Then 200 OK
And response contains Keycloak identity attributes
And response contains PostgreSQL theme
```

##### **TC-02 — Another User**

```text
Given authenticated user A

When GET /users/B

Then 403 Forbidden
```

##### **TC-03 — No Organization**

```text
Given user has 0 organizations

Then hasOrganization = false
```

##### **TC-04 — Organization Exists**

```text
Given user has 1 organization

Then hasOrganization = true
```

##### **TC-05 — Multiple Organizations**

```text
Given user has N organizations

Then hasOrganization = true
```

## References