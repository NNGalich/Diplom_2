package steps;

import java.util.ArrayList;
import java.util.List;

import client.StellarBurgersTestClient;
import dto.CreateOrderResponseDto;
import dto.ListIngredientsResponseIngredientDto;
import dto.ListOrdersResponseDto;
import dto.ListOrdersResponseOrderDto;
import dto.UserLoginResponseDto;
import dto.ListIngredientsResponseDto;
import dto.UserUpdateResponseDto;
import io.qameta.allure.Step;
import org.hamcrest.CoreMatchers;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class StellarBurgersTestSteps {

    private final StellarBurgersTestClient client;

    public StellarBurgersTestSteps(StellarBurgersTestClient client) {
        this.client = client;
    }

    @Step("List ingredients")
    public ListIngredientsResponseDto listIngredients() {
        return client.listIngredients()
                .statusCode(200)
                .extract()
                .as(ListIngredientsResponseDto.class);
    }

    @Step("Create order for user")
    public CreateOrderResponseDto createOrderForUser(String accessToken, List<String> ingredients) {
        return client.createOrder(accessToken, ingredients)
                .statusCode(200)
                .extract()
                .as(CreateOrderResponseDto.class);
    }

    @Step("Create some random order with user")
    public CreateOrderResponseDto createSomeOrderWithUser(String accessToken, int numIngredients) {
        List<String> ingredients = getSomeIngredientsForNewOrder(1, numIngredients - 2, 1);
        return client.createOrder(accessToken, ingredients)
                .statusCode(200)
                .extract()
                .as(CreateOrderResponseDto.class);
    }

    @Step("Create order without user")
    public CreateOrderResponseDto createOrderWithoutUser(List<String> ingredients) {
        return client.createOrder(null, ingredients)
                .statusCode(200)
                .extract()
                .as(CreateOrderResponseDto.class);
    }

    @Step("Create some random order without user")
    public CreateOrderResponseDto createSomeOrderWithoutUser(int numIngredients) {
        List<String> ingredients = getSomeIngredientsForNewOrder(1, numIngredients - 2, 1);
        return client.createOrder(null, ingredients)
                .statusCode(200)
                .extract()
                .as(CreateOrderResponseDto.class);
    }

    @Step("Get some ingredient hashes for new order")
    public List<String> getSomeIngredientsForNewOrder(int numBuns, int numMains, int numSauces) {
        ListIngredientsResponseDto allIngredients = listIngredients();
        List<String> result = new ArrayList<>(numBuns + numMains + numSauces);
        for (ListIngredientsResponseIngredientDto ingredientDto : allIngredients.getData()) {
            if (ingredientDto.getType().equals("bun") && numBuns > 0) {
                result.add(ingredientDto.getId());
                --numBuns;
            }
        }
        for (ListIngredientsResponseIngredientDto ingredientDto : allIngredients.getData()) {
            if (ingredientDto.getType().equals("main") && numMains > 0) {
                result.add(ingredientDto.getId());
                --numMains;
            }
        }
        for (ListIngredientsResponseIngredientDto ingredientDto : allIngredients.getData()) {
            if (ingredientDto.getType().equals("sauce") && numSauces > 0) {
                result.add(ingredientDto.getId());
                --numSauces;
            }
        }
        return result;
    }

    @Step("Create user")
    public UserLoginResponseDto createUser(String email, String password, String name) {
        return client.createUser(email, password, name)
                .statusCode(200)
                .extract()
                .as(UserLoginResponseDto.class);
    }

    @Step("Login user")
    public UserLoginResponseDto loginUser(String email, String password) {
        return client.userAuthorization(email, password)
                .statusCode(200)
                .extract()
                .as(UserLoginResponseDto.class);
    }

    @Step("Logout user")
    public void logoutUser(String refreshToken) {
        client.userLogout(refreshToken)
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Step("Delete user")
    public void deleteUser(String accessToken) {
        client.userDelete(accessToken)
                .statusCode(anyOf(equalTo(200), equalTo(202)));
    }

    @Step("Update user")
    public UserUpdateResponseDto updateUser(String accessToken, String email, String password, String name) {
        return client.userUpdate(accessToken, email, password, name)
                .statusCode(200)
                .extract()
                .as(UserUpdateResponseDto.class);
    }

    @Step("List all orders")
    public ListOrdersResponseDto listAllOrders() {
        return client.listAllOrders()
                .statusCode(200)
                .extract()
                .as(ListOrdersResponseDto.class);
    }

    @Step("List user orders")
    public ListOrdersResponseDto listUserOrders(String accessToken) {
        return client.listOrdersForUser(accessToken)
                .statusCode(200)
                .extract()
                .as(ListOrdersResponseDto.class);
    }

    @Step("Find an order by its number")
    public ListOrdersResponseOrderDto findOrderWithNumber(String accessToken, int orderNumber) {
        ListOrdersResponseDto ordersListResponse;
        if (accessToken == null) {
            ordersListResponse = listAllOrders();
        } else {
            ordersListResponse = listUserOrders(accessToken);
        }

        for (ListOrdersResponseOrderDto order : ordersListResponse.getOrders()) {
            if (order.getNumber() == orderNumber) {
                return order;
            }
        }

        return null;
    }

    @Step("Delete user if exists")
    public void deleteUserIfExists(String email, String password) {
        try {
            UserLoginResponseDto response = loginUser(email, password);
            deleteUser(response.getAccessToken());
        } catch (AssertionError e) {
        }
    }
}
