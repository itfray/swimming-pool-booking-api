package pro.itfray.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.itfray.AbstractE2ETest;
import pro.itfray.domain.Theme;
import pro.itfray.domain.User;
import pro.itfray.repository.UserRepository;

class UserControllerE2ETest extends AbstractE2ETest {

  static final String URL = "/v1/users";

  static String USERNAME = "test";
  static String PASSWORD = "test";

  static final String USER_ID = "a4c7e2d6-e7f7-4323-8360-466f1f5c5e26";
  static final String EMAIL = "test@testmail.com";
  static final String FIRST_NAME = "Test";
  static final String LAST_NAME = "Test";
  static final boolean HAS_ORGANIZATION = false;

  String token;

  @Autowired
  UserRepository repository;

  @BeforeEach
  void setUpToken() {
    token = getToken(USERNAME, PASSWORD);
  }

  @BeforeEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("Should create a user and return a user profile")
  void shouldCreateUserAndReturnUserProfile() {
    given()
        .header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .when()
        .post(URL)
        .then()
        .statusCode(201)
        .body("uid", equalTo(USER_ID))
        .body("username", equalTo(USERNAME))
        .body("email", equalTo(EMAIL))
        .body("firstName", equalTo(FIRST_NAME))
        .body("lastName", equalTo(LAST_NAME))
        .body("hasOrganization", equalTo(HAS_ORGANIZATION))
        .body("theme", equalTo(Theme.WHITE.name()));
  }

  @Test
  @DisplayName("Should return a user profile of an existing user")
  void shouldGetExistingUser() {
    repository.save(new User(UUID.fromString(USER_ID), Theme.BLACK));

    given()
        .header("Authorization", "Bearer " + token)
        .contentType("application/json")
        .when()
        .get(URL + "/" + USER_ID)
        .then()
        .statusCode(200)
        .body("uid", equalTo(USER_ID))
        .body("username", equalTo(USERNAME))
        .body("email", equalTo(EMAIL))
        .body("firstName", equalTo(FIRST_NAME))
        .body("lastName", equalTo(LAST_NAME))
        .body("hasOrganization", equalTo(HAS_ORGANIZATION))
        .body("theme", equalTo(Theme.BLACK.name()));
  }
}
