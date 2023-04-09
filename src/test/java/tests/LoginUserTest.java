package tests;

import client.StellarBurgersTestClient;
import constants.ServerUrl;
import constants.Users;
import dto.UserLoginResponseDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {

    private StellarBurgersTestClient client;
    private StellarBurgersTestSteps steps;

    private UserLoginResponseDto user;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new StellarBurgersTestClient();
        steps = new StellarBurgersTestSteps(client);

        steps.deleteUserIfExists(Users.EMAIL, Users.PASSWORD);
        user = steps.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME);
    }

    @Test
    @DisplayName("Log into existing user returns 200 and user info")
    public void loginInExistingUserReturns200AndUserInfo() {
        client.userAuthorization(Users.EMAIL, Users.PASSWORD)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.EMAIL))
                .body("user.name", equalTo(Users.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Log in existing user with invalid password returns 401")
    public void loginInWithInvalidPasswordReturns401() {
        client.userAuthorization(Users.EMAIL, Users.ANOTHER_PASSWORD)
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Log in existing user with invalid email returns 401")
    public void loginInWithInvalidEmailReturns401() {
        client.userAuthorization(Users.ANOTHER_EMAIL, Users.PASSWORD)
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        steps.deleteUser(user.getAccessToken());
    }
}
