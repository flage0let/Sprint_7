package ru.praktikumservices.qascooter;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.courier.CourierClient;
import ru.praktikumservices.qascooter.models.Courier;
import ru.praktikumservices.qascooter.models.CourierCreds;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.praktikumservices.qascooter.courier.CourierGenerator.*;

public class LoginCourierTest {

    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Авторизация курьера с валидными данными")
    public void loginCourier() {
        Courier courier = randomCourier();

        createCourierAccount(courierClient, courier);
        Response response = sendLoginCourierRequest(courierClient, courier);
        checkLoginResponseStatusCode(response);
        checkLoginSuccessResponseBody(response);
        deleteCourierAccount(id);
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Авторизация курьера с невалидными данными")
    public void loginInvalidDataCourier() {
        Courier courier = randomCourier();

        Response response = sendLoginCourierRequest(courierClient, courier);
        checkLoginInvalidDataResponseStatusCode(response);
        checkLoginInvalidDataResponseBody(response);
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Авторизация курьера без логина")
    public void loginWithoutLoginCourier() {
        Courier courier = randomCourierWithoutLogin();

        Response response = sendLoginCourierRequest(courierClient, courier);
        checkLoginWithoutDataResponseStatusCode(response);
        checkLoginWithoutDataResponseBody(response);
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Авторизация курьера без пароля")
    public void loginWithoutPasswordCourier() {
        Courier courier = randomCourierWithoutPassword();

        Response response = sendLoginCourierRequest(courierClient, courier);
        checkLoginWithoutDataResponseStatusCode(response);
        checkLoginWithoutDataResponseBody(response);
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Авторизация курьера без логина и пароля")
    public void loginWithoutLoginAndPasswordCourier() {
        Courier courier = randomCourierWithoutLoginAndPassword();

        Response response = sendLoginCourierRequest(courierClient, courier);
        checkLoginWithoutDataResponseStatusCode(response);
        checkLoginWithoutDataResponseBody(response);
    }


    @Step("Create courier account, check response status code and response body")
    public Response createCourierAccount(CourierClient courierClient, Courier courier) {
        Response response = courierClient.signUp(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        assertEquals("Некорректный статус операции", true, response.path("ok"));
        return response;
    }

    @Step("Send POST request to api/v1/courier/logIn")
    public Response sendLoginCourierRequest(CourierClient courierClient, Courier courier) {
        return courierClient.logIn(CourierCreds.credsFromCourier(courier));
    }

    @Step("Check logIn success response status code")
    public void checkLoginResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
    }

    @Step("Check logIn success response body")
    private void checkLoginSuccessResponseBody(Response response) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("SchemaLoginCourierResponse.json"));
        id = response.path("id");
        assertTrue(id > 0);
    }

    @Step("Check logIn invalid data response status code")
    private void checkLoginInvalidDataResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
    }

    @Step("Check logIn invalid data response body")
    private void checkLoginInvalidDataResponseBody(Response response) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("SchemaErrorResponse.json"));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Check logIn without data response status code")
    private void checkLoginWithoutDataResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
    }

    @Step("Check logIn without data response body")
    private void checkLoginWithoutDataResponseBody(Response response) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath("SchemaErrorResponse.json"));
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Delete courier account")
    private void deleteCourierAccount(int id) {
        courierClient.delete(id);
    }
}
