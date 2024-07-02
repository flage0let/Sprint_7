package ru.praktikumservices.qascooter.courier;

import io.restassured.response.Response;
import ru.praktikumservices.qascooter.models.Courier;
import ru.praktikumservices.qascooter.models.CourierCreds;
import ru.praktikumservices.qascooter.specification.Endpoints;

import static io.restassured.RestAssured.given;
import static ru.praktikumservices.qascooter.specification.Specifications.requestSpecification;

public class CourierClient {

    public Response signUp(Courier courier) {
        return given().log().all()
                .spec(requestSpecification())
                .body(courier)
                .when()
                .post(Endpoints.CREATE_ENDPOINT);
    }

    public Response logIn(CourierCreds creds) {
        return given().log().all()
                .spec(requestSpecification())
                .body(creds)
                .when()
                .post(Endpoints.LOGIN_ENDPOINT);
    }

    public Response delete(int id) {
        return given().log().all()
                .spec(requestSpecification())
                .pathParam("id", id) //Добавляем параметр в путь /api/v1/courier/{id}
                .body(String.format("{\"id\": \"%d\"}", id))
                .when()
                .delete(Endpoints.DELETE_ENDPOINT);
    }
}

