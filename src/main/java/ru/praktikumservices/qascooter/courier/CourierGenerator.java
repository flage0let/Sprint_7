package ru.praktikumservices.qascooter.courier;

import ru.praktikumservices.qascooter.models.Courier;

import static ru.praktikumservices.qascooter.utils.Utils.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin(randomString(10))
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }

    public static Courier randomCourierWithoutData() {
        return new Courier();
    }

    public static Courier randomCourierWithoutLogin() {
        return new Courier()
                .withPassword(randomString(12))
                .withFirstName(randomString(20));
    }

    public static Courier randomCourierWithoutPassword() {
        return new Courier()
                .withLogin(randomString(10))
                .withFirstName(randomString(20));
    }

    public static Courier randomCourierWithoutLoginAndPassword() {
        return new Courier()
                .withFirstName(randomString(20));
    }
}
