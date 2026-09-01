package com.payment.automation.client;

import com.payment.automation.config.ConfigManager;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseApiClient {

    protected RequestSpecification getRequestSpecification() {

        return given()
                .baseUri(ConfigManager.get("application.base.url"))
                .contentType("application/json")
                .accept("application/json");
    }

    protected RequestSpecification getAuthenticatedRequestSpecification() {

        return getRequestSpecification()
                .header(
                        "Authorization",
                        "Bearer " + ConfigManager.get("auth.token")
                );
    }
}