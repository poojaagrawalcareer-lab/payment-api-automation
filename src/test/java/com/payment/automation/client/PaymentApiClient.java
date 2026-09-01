package com.payment.automation.client;

import com.payment.automation.config.ConfigManager;
import com.payment.automation.model.PaymentRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PaymentApiClient extends BaseApiClient {

    public Response createPayment(PaymentRequest paymentRequest) {

        return getAuthenticatedRequestSpecification()
                .body(paymentRequest)
                .when()
                .post("/payments");
    }
    public Response createPaymentWithInvalidToken(
            PaymentRequest paymentRequest) {

        return given()
                .baseUri(ConfigManager.get("application.base.url"))
                .contentType("application/json")
                .accept("application/json")
                .header("Authorization", "Bearer invalid-token")
                .body(paymentRequest)
                .when()
                .post("/payments");
    }

    public Response createPaymentWithoutToken(
            PaymentRequest paymentRequest) {

        return given()
                .baseUri(ConfigManager.get("application.base.url"))
                .contentType("application/json")
                .accept("application/json")
                .body(paymentRequest)
                .when()
                .post("/payments");
    }
}

