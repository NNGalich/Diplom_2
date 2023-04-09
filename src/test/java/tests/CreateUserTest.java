package tests;

import client.StellarBurgersTestClient;
import constants.Users;
import constants.ServerUrl;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {

    private StellarBurgersTestClient client;
    private StellarBurgersTestSteps steps;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new StellarBurgersTestClient();
        steps = new StellarBurgersTestSteps(client);

        steps.deleteUserIfExists(Users.EMAIL, Users.PASSWORD);
    }

    @Test
    @DisplayName("Check that user can be created")
    public void checkThatUserCanBeCreated() {
        client.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.EMAIL))
                .body("user.name", equalTo(Users.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Check that users with identical emails can't be created")
    public void createUsersWithSameEmailReturnsAnError() {
        steps.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME);
        client.createUser(Users.EMAIL, Users.ANOTHER_PASSWORD, Users.NAME)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check that users with identical data can't be created")
    public void createUsersWithSameDataReturnsAnError() {
        steps.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME);
        client.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Check that user without email can't be created")
    public void createUserWithoutEmailReturnsAnError() {
        client.createUser(null, Users.PASSWORD, Users.NAME)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check that user without password can't be created")
    public void createUserWithoutPasswordReturnsAnError() {
        client.createUser(Users.EMAIL, null, Users.NAME)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Check that user without name can't be created")
    public void createUserWithoutNameReturnsAnError() {
        client.createUser(Users.EMAIL, Users.PASSWORD, null)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        steps.deleteUserIfExists(Users.EMAIL, Users.PASSWORD);
    }
}
