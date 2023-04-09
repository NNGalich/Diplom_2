package tests;

import java.util.ArrayList;
import java.util.List;

import client.StellarBurgersTestClient;
import constants.ServerUrl;
import dto.CreateOrderResponseDto;
import dto.ListOrdersResponseOrderDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateOrderWithoutAuthorizationTest {

    private StellarBurgersTestClient client;
    private StellarBurgersTestSteps steps;

    @Before
    public void setUp() {
        RestAssured.baseURI = ServerUrl.SERVER_URL;
        client = new StellarBurgersTestClient();
        steps = new StellarBurgersTestSteps(client);
    }

    @Test
    @DisplayName("Create order with valid ingredients returns 200")
    public void createOrderWithValidIngredientsReturns200() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(1, 2, 1);

        client.createOrder(null, ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .extract()
                .as(CreateOrderResponseDto.class);
    }

    @Test
    @DisplayName("Create order with valid ingredients but without bun returns 200")
    public void createOrderWithValidIngredientsButWithoutBunReturns200() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(0, 3, 1);

        client.createOrder(null, ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Create order with sauses only returns 200")
    public void createOrderSausesOnlyReturns200() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(0, 0, 3);

        client.createOrder(null, ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Create order without ingredients returns 400")
    public void createOrderWithoutIngredientsReturns400() {
        client.createOrder(null, new ArrayList<>())
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with invalid ingredient returns 500")
    public void createOrderWithInvalidIngredientReturns500() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(1, 2, 1);
        ingredients.add("broken-id");

        client.createOrder(null, ingredients)
                .statusCode(500);
    }
}
