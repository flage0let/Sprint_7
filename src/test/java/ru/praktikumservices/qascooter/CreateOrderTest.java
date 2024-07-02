package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.models.Order;
import ru.praktikumservices.qascooter.models.OrderResponse;
import ru.praktikumservices.qascooter.specification.Endpoints;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static ru.praktikumservices.qascooter.specification.Specifications.requestSpecification;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    Order order;

    @Parameterized.Parameter
    public String firstName;

    @Parameterized.Parameter(1)
    public String lastName;

    @Parameterized.Parameter(2)
    public String address;

    @Parameterized.Parameter(3)
    public String metroStation;

    @Parameterized.Parameter(4)
    public String phone;

    @Parameterized.Parameter(5)
    public int rentTime;

    @Parameterized.Parameter(6)
    public String deliveryDate;

    @Parameterized.Parameter(7)
    public String comment;

    @Parameterized.Parameter(8)
    public String[] color;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {"Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        "4",
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{}
                },
                {"Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        "4",
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{"BLACK"}
                },
                {"Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        "4",
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{"GREY"}
                },
                {"Naruto",
                        "Uchiha",
                        "Konoha, 142 apt.",
                        "4",
                        "+7 800 355 35 35",
                        5,
                        "2020-06-06",
                        "Saske, come back to Konoha",
                        new String[]{"BLACK", "GREY"}
                }
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с комбинациями цветов: BLACK и GREY")
    public void createOrder() {
        order = new Order(firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);

        Response response = sendCreateOrderRequest();
        checkResponseStatusCode(response);
        checkResponseBody(response);
    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendCreateOrderRequest() {
        return given().log().all()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(Endpoints.ORDERS_ENDPOINT);
    }

    @Step("Check response status code")
    public void checkResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
    }

    @Step("Check response status code")
    public void checkResponseBody(Response response) {
        OrderResponse orderResponse = response.then().extract().as(OrderResponse.class);
        assertFalse(orderResponse.getTrack().isEmpty());
    }
}
