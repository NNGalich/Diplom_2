package tests;

import java.util.ArrayList;
import java.util.List;

import client.StellarBurgersTestClient;
import constants.ServerUrl;
import constants.Users;
import dto.CreateOrderResponseDto;
import dto.ListOrdersResponseOrderDto;
import dto.UserLoginResponseDto;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateOrderWithAuthorizationTest {

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
    @DisplayName("Create order with valid ingredients returns 200 and creates order")
    public void createOrderWithValidIngredientsReturns200AndCreatesOrder() throws InterruptedException {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(1, 2, 1);

        CreateOrderResponseDto responseDto = client.createOrder(user.getAccessToken(), ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .extract()
                .as(CreateOrderResponseDto.class);

        checkOrderPresentInOrdersList(responseDto, ingredients);
    }

    @Test
    @DisplayName("Create order with valid ingredients but without bun returns 200 and creates order")
    public void createOrderWithValidIngredientsButWithoutBunReturns200AndCreatesOrder() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(0, 3, 1);

        CreateOrderResponseDto responseDto = client.createOrder(user.getAccessToken(), ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue())
                .extract()
                .as(CreateOrderResponseDto.class);

        checkOrderPresentInOrdersList(responseDto, ingredients);
    }

    @Test
    @DisplayName("Create order with sauses only returns 200 and creates order")
    public void createOrderSausesOnlyReturns200AndCreatesOrder() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(0, 0, 3);

        CreateOrderResponseDto responseDto = client.createOrder(user.getAccessToken(), ingredients)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue())
                .extract()
                .as(CreateOrderResponseDto.class);

        checkOrderPresentInOrdersList(responseDto, ingredients);
    }

    @Test
    @DisplayName("Create order without ingredients returns 400 and doesn't create order")
    public void createOrderWithoutIngredientsReturns400AndDoesntCreatesOrder() {
        client.createOrder(user.getAccessToken(), new ArrayList<>())
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));

        checkUserHasNoOrders();
    }

    @Test
    @DisplayName("Create order with invalid ingredient returns 500 and doesn't create order")
    public void createOrderWithInvalidIngredientReturns500AndDoesntCreateOrder() {
        List<String> ingredients = steps.getSomeIngredientsForNewOrder(1, 2, 1);
        ingredients.add("broken-id");

        client.createOrder(user.getAccessToken(), ingredients)
                .statusCode(500);

        checkUserHasNoOrders();
    }

    @Step("Check new order was created")
    private void checkOrderPresentInOrdersList(CreateOrderResponseDto createOrderResponseDto, List<String> ingredients) {
        ListOrdersResponseOrderDto orderDto = steps.findOrderWithNumber(
                user.getAccessToken(),
                createOrderResponseDto.getOrder().getNumber()
        );
        assertThat(orderDto, notNullValue());
        assertThat(orderDto.getIngredients(), equalTo(ingredients));
        assertThat(orderDto.getName(), equalTo(createOrderResponseDto.getName()));
    }

    @Step("Check user has no orders")
    private void checkUserHasNoOrders() {
        assertThat(steps.listUserOrders(user.getAccessToken()).getOrders().size(), equalTo(0));
    }

    @After
    public void tearDown() {
        steps.deleteUser(user.getAccessToken());
    }
}
