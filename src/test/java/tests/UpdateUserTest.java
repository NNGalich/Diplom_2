package tests;

import client.StellarBurgersTestClient;
import constants.ServerUrl;
import constants.Users;
import dto.UserLoginResponseDto;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateUserTest {


    private StellarBurgersTestClient client;
    private StellarBurgersTestSteps steps;

    private UserLoginResponseDto user;
    private UserLoginResponseDto anotherUser;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new StellarBurgersTestClient();
        steps = new StellarBurgersTestSteps(client);

        steps.deleteUserIfExists(Users.EMAIL, Users.PASSWORD);
        steps.deleteUserIfExists(Users.ANOTHER_EMAIL, Users.ANOTHER_PASSWORD);

        user = steps.createUser(Users.EMAIL, Users.PASSWORD, Users.NAME);
        anotherUser = steps.createUser(Users.ANOTHER_EMAIL, Users.ANOTHER_PASSWORD, Users.ANOTHER_NAME);
    }

    @Test
    @DisplayName("Update user's email returns 200 and email is updated")
    public void updateEmailFieldOnExisingUserReturns200AndUpdatedField() {
        client.userUpdate(user.getAccessToken(), Users.UPDATED_EMAIL, null, null)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.UPDATED_EMAIL))
                .body("user.name", equalTo(Users.NAME));

        refreshUserWithNewData(Users.UPDATED_EMAIL, Users.PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.UPDATED_EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.NAME));
    }

    @Test
    @DisplayName("Update user's password returns 200 and password is updated")
    public void updatePasswordFieldOnExisingUserReturns200AndUpdatedField() {
        client.userUpdate(user.getAccessToken(), null, Users.UPDATED_PASSWORD, null)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.EMAIL))
                .body("user.name", equalTo(Users.NAME));

        refreshUserWithNewData(Users.EMAIL, Users.UPDATED_PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.NAME));
    }

    @Test
    @DisplayName("Update user's name returns 200 and name is updated")
    public void updateNameFieldOnExisingUserReturns200AndUpdatedField() {
        client.userUpdate(user.getAccessToken(), null, null, Users.UPDATED_NAME)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.EMAIL))
                .body("user.name", equalTo(Users.UPDATED_NAME));

        refreshUserWithNewData(Users.EMAIL, Users.PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.UPDATED_NAME));
    }

    @Test
    @DisplayName("Update user's name returns 200 and all fields are updated")
    public void updateAllFieldsOnExisingUserReturns200AndUpdatedField() {
        client.userUpdate(user.getAccessToken(), Users.UPDATED_EMAIL, Users.UPDATED_PASSWORD, Users.UPDATED_NAME)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("user.email", equalTo(Users.UPDATED_EMAIL))
                .body("user.name", equalTo(Users.UPDATED_NAME));

        refreshUserWithNewData(Users.UPDATED_EMAIL, Users.UPDATED_PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.UPDATED_EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.UPDATED_NAME));
    }

    @Test
    @DisplayName("Update with invalid access token does nothing")
    public void updateOnInvalidUserReturns401AndNotAuthrorizedMessage() {
        client.userUpdate("not-existed-token", Users.UPDATED_EMAIL, Users.UPDATED_PASSWORD, Users.UPDATED_NAME)
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Update email to already used email returns 403 and does nothing")
    public void updateEmailToAlreadyUsedReturns403AndNothingChanged() {
        client.userUpdate(user.getAccessToken(), Users.ANOTHER_EMAIL, null, null)
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));

        refreshUserWithNewData(Users.EMAIL, Users.PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.NAME));
    }

    @Test
    @DisplayName("Update all fields with email to already used email returns 403 and does nothing")
    public void updateAllFieldsWithEmailToAlreadyUsedReturns403AndNothingChanged() {
        client.userUpdate(user.getAccessToken(), Users.ANOTHER_EMAIL, Users.UPDATED_PASSWORD, Users.UPDATED_NAME)
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));

        refreshUserWithNewData(Users.EMAIL, Users.PASSWORD);

        assertThat(user.getUser().getEmail(), equalTo(Users.EMAIL));
        assertThat(user.getUser().getName(), equalTo(Users.NAME));
    }

    @Step("Log into updated user and refresh")
    private void refreshUserWithNewData(String email, String password) {
        user = steps.loginUser(email, password);
    }

    @After
    public void tearDown() {
        steps.deleteUser(user.getAccessToken());
        steps.deleteUser(anotherUser.getAccessToken());
    }
}
