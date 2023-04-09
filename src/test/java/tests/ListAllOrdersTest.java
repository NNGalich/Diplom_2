package tests;

import client.StellarBurgersTestClient;
import constants.ServerUrl;
import constants.Users;
import dto.CreateOrderResponseDto;
import dto.ListOrdersResponseDto;
import dto.ListOrdersResponseOrderDto;
import dto.UserLoginResponseDto;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.StellarBurgersTestSteps;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ListAllOrdersTest {

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
    @DisplayName("List all orders returns 50 orders with correct schema")
    public void listAllOrdersReturns50OrdersWithCorrectSchema() {
        client.listAllOrders()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .body("orders.size()", equalTo(50))
                .extract()
                .as(ListOrdersResponseDto.class);
    }

    @Test
    @DisplayName("List all orders doesn't show anonymous orders")
    public void listAllOrdersDoesntReturnAnonymousOrders() {
        CreateOrderResponseDto createOrderResponseDto = steps.createSomeOrderWithoutUser(4);

        ListOrdersResponseOrderDto orderDto = steps.findOrderWithNumber(
                null,
                createOrderResponseDto.getOrder().getNumber()
        );

        assertThat(orderDto, nullValue());
    }

    @Test
    @DisplayName("List all orders shows users' orders")
    public void listAllOrdersReturnsOrderCreatedByUser() {
        CreateOrderResponseDto createOrderResponseDto = steps.createSomeOrderWithUser(user.getAccessToken(), 4);

        ListOrdersResponseOrderDto orderDto = steps.findOrderWithNumber(
                null,
                createOrderResponseDto.getOrder().getNumber()
        );

        assertThat(orderDto, notNullValue());
    }

    @After
    public void tearDown() {
        steps.deleteUser(user.getAccessToken());
    }
}
