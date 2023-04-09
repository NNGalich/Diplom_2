package client;

import java.util.List;

import dto.CreateOrderRequestDto;
import dto.UserAuthorizationRequestDto;
import dto.UserCreateRequestDto;
import dto.UserLogoutRequestDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class StellarBurgersTestClient {

    @Step("GET /api/ingredients")
    public ValidatableResponse listIngredients() {
        return given()
                .log()
                .all()
                .when()
                .get("/api/ingredients")
                .then()
                .log()
                .all();
    }

    @Step("POST /api/orders")
    public ValidatableResponse createOrder(String accessToken, List<String> ingredients) {
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto(ingredients);
        RequestSpecification request = given();
        if (accessToken != null) {
            request = request.header("Authorization", accessToken).and();
        }
        return request
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/orders")
                .then()
                .log()
                .all();
    }

    @Step("POST /api/auth/register")
    public ValidatableResponse createUser(String email, String password, String name) {
        UserCreateRequestDto requestDto = new UserCreateRequestDto(email, password, name);
        return given()
                .header("Content-type", "application/json" )
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/auth/register")
                .then()
                .log()
                .all();
    }

    @Step("POST /api/auth/login")
    public ValidatableResponse userAuthorization(String email, String password) {
        UserAuthorizationRequestDto requestDto = new UserAuthorizationRequestDto(email, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/auth/login")
                .then()
                .log()
                .all();
    }

    @Step("POST /api/auth/logout")
    public ValidatableResponse userLogout(String refreshToken) {
        UserLogoutRequestDto requestDto = new UserLogoutRequestDto(refreshToken);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .post("/api/auth/logout")
                .then()
                .log()
                .all();
    }

    @Step("DELETE /api/auth/user")
    public ValidatableResponse userDelete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .log()
                .all();
    }

    @Step("PATCH /api/auth/user")
    public ValidatableResponse userUpdate(String accessToken, String email, String password, String name) {
        UserCreateRequestDto requestDto = new UserCreateRequestDto(email, password, name);
        RequestSpecification request = given();
        if (accessToken != null) {
            request = request.header("Authorization", accessToken).and();
        }
        return request
                .header("Content-Type", "application/json")
                .and()
                .body(requestDto)
                .log()
                .all()
                .when()
                .patch("/api/auth/user")
                .then()
                .log()
                .all();
    }

    @Step("GET /api/orders/all")
    public ValidatableResponse listAllOrders() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .log()
                .all()
                .when()
                .get("/api/orders/all")
                .then()
                .log()
                .all();
    }

    @Step("GET /api/orders")
    public ValidatableResponse listOrdersForUser(String accessToken) {
        RequestSpecification request = given();

        if (accessToken != null) {
            request = request.header("Authorization", accessToken).and();
        }
        return request
                .header("Content-type", "application/json")
                .and()
                .log()
                .all()
                .when()
                .get("/api/orders")
                .then()
                .log()
                .all();
    }
}
