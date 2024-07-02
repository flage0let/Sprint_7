package ru.praktikumservices.qascooter.specification;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("https://qa-scooter.praktikum-services.ru")
                .setContentType(ContentType.JSON)
                .build();
    }
}
