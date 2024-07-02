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

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static ru.praktikumservices.qascooter.courier.CourierGenerator.*;

public class SignupСourierTest {

    private CourierClient courierClient;
    private int id;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с валидными данными")
    public void signUpValidCourier() {
        Courier courier = randomCourier();

        Response createResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpSuccessResponseStatusCode(createResponse);
        checkSignUpSuccessResponseBody(createResponse);
        checkLoginNewAccount(courierClient, courier);
        deleteCourierAccount(id);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с повторяющимся логином")
    public void signUpCloneCourier() {
        Courier courier = randomCourier();

        Response createResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpSuccessResponseStatusCode(createResponse);
        checkSignUpSuccessResponseBody(createResponse);

        Response createCloneResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpCloneResponseStatusCode(createCloneResponse);
        checkSignUpCloneResponseBody(createCloneResponse);

        deleteCourierAccount(id);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера без логина")
    public void signUpWithoutLoginCourier() {
        Courier courier = randomCourierWithoutLogin();

        Response createWithoutDataResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpWithoutDataResponseStatusCode(createWithoutDataResponse);
        checkSignUpWithoutDataResponseBody(createWithoutDataResponse);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера без пароля")
    public void signUpWithoutPasswordCourier() {
        Courier courier = randomCourierWithoutPassword();

        Response createWithoutDataResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpWithoutDataResponseStatusCode(createWithoutDataResponse);
        checkSignUpWithoutDataResponseBody(createWithoutDataResponse);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера без логина и пароля")
    public void signUpWithoutLoginAndPasswordCourier() {
        Courier courier = randomCourierWithoutLoginAndPassword();

        Response createWithoutDataResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpWithoutDataResponseStatusCode(createWithoutDataResponse);
        checkSignUpWithoutDataResponseBody(createWithoutDataResponse);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера без логина, пароля и имени")
    public void signUpWithoutAllDataCourier() {
        Courier courier = randomCourierWithoutData();

        Response createWithoutDataResponse = sendSignUpCourierRequest(courierClient, courier);
        checkSignUpWithoutDataResponseStatusCode(createWithoutDataResponse);
        checkSignUpWithoutDataResponseBody(createWithoutDataResponse);
    }


    @Step("Send POST request to /api/v1/courier")
    public Response sendSignUpCourierRequest(CourierClient courierClient, Courier courier) {
        return courierClient.signUp(courier);
    }

    @Step("Check sign up success response status code")
    public void checkSignUpSuccessResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
    }

    @Step("Check sign up success response body")
    public void checkSignUpSuccessResponseBody(Response response) {
        assertEquals("Некорректный статус операции", true, response.path("ok"));
    }

    @Step("Check logIn new account")
    public Response checkLoginNewAccount(CourierClient courierClient, Courier courier) {
        Response response = courierClient.logIn(CourierCreds.credsFromCourier(courier));
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        id = response.path("id");
        return response;
    }

    @Step("Check sign up clone response status code")
    private void checkSignUpCloneResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_CONFLICT, response.statusCode());
    }

    @Step("Check sign up clone response body")
    private void checkSignUpCloneResponseBody(Response response) {
        assertEquals("Неверное сообщение", "Этот логин уже используется", response.path("message"));
    }

    @Step("Check sign up without data response status code")
    private void checkSignUpWithoutDataResponseStatusCode(Response response) {
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
    }

    @Step("Check sign up without data response body")
    private void checkSignUpWithoutDataResponseBody(Response response) {
        assertEquals("Неверное сообщение", "Недостаточно данных для создания учетной записи", response.path("message"));
    }

    @Step
    private void deleteCourierAccount(int id) {
        courierClient.delete(id);
    }
}
