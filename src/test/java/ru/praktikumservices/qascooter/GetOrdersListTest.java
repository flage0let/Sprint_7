package ru.praktikumservices.qascooter;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.courier.CourierClient;
import ru.praktikumservices.qascooter.models.Courier;
import ru.praktikumservices.qascooter.models.CourierCreds;
import ru.praktikumservices.qascooter.models.OrderListResponse;
import ru.praktikumservices.qascooter.specification.Endpoints;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.praktikumservices.qascooter.courier.CourierGenerator.randomCourier;
import static ru.praktikumservices.qascooter.specification.Specifications.requestSpecification;

public class GetOrdersListTest {

    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        Courier courier = randomCourier();
        createCourierAccount(courierClient, courier);
        loginCourierAccountAndGetId(courierClient, courier);
    }

    @Test
    public void getOrdersList() {
        Response response = sendGetOrderListRequest();
        checkResponseStatusCode(response);
        checkResponseBody(response);
    }

    @After
    public void TearDown() {
        courierClient.delete(id);
    }

    @Step("Create courier account, check response status code and body")
    public Response createCourierAccount(CourierClient courierClient, Courier courier) {
        Response response = courierClient.signUp(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        assertEquals("Некорректный статус операции", true, response.path("ok"));
        return response;
    }

    @Step("Login courier account, get courier id, check response status code and body")
    private void loginCourierAccountAndGetId(CourierClient courierClient, Courier courier) {
        Response response = courierClient.logIn(CourierCreds.credsFromCourier(courier));
        response.then().assertThat().statusCode(SC_OK);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("SchemaLoginCourierResponse.json"));
        id = response.path("id");
        assertTrue(id > 0);
    }

    @Step("Send GET request to /api/v1/orders")
    public Response sendGetOrderListRequest() {
        return given().log().all()
                .spec(requestSpecification())
                .queryParam("courierId", id)
                .when()
                .get(Endpoints.ORDERS_ENDPOINT);
    }

    @Step("Check response status code")
    private void checkResponseStatusCode(Response response) {
        response.then().assertThat().statusCode(SC_OK);
    }

    @Step("Check response body")
    private void checkResponseBody(Response response) {
        OrderListResponse orderListResponse = response.then().extract().as(OrderListResponse.class);
        MatcherAssert.assertThat(orderListResponse, notNullValue());
    }
}
