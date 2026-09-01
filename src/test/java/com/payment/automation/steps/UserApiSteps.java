package com.payment.automation.steps;

import com.payment.automation.client.PaymentApiClient;
import com.payment.automation.context.ScenarioContext;
import com.payment.automation.model.PaymentRequest;
import com.payment.automation.model.PaymentResponse;
import com.payment.automation.testdata.PaymentTestDataBuilder;
import com.payment.automation.testdata.PaymentTestDataFactory;
import com.payment.automation.wiremock.WireMockManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiSteps {

    private static final Logger log =
            LoggerFactory.getLogger(UserApiSteps.class);

    private final ScenarioContext scenarioContext;

    private String merchantId;
    private int amount;
    private String currency;

    private final PaymentApiClient paymentApiClient =
            new PaymentApiClient();

    public UserApiSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @When("I send a payment request with {string}, {int}, and {string}")
    public void sendPaymentRequest(
            String merchantId,
            int amount,
            String currency) {

        log.info("========== PAYMENT STEP STARTED ==========");

        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;

        PaymentRequest paymentRequest =
                PaymentTestDataFactory.createPayment(
                        merchantId,
                        amount,
                        currency
                );

        Response response =
                paymentApiClient.createPayment(paymentRequest);

        scenarioContext.setResponse(response);

        log.info(
                "Payment request: merchantId={}, amount={}, currency={}",
                merchantId,
                amount,
                currency
        );

        log.info("Response Status: {}", response.statusCode());
        log.info("Response Body: {}", response.asPrettyString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int statusCode) {

        scenarioContext.getResponse()
                .then()
                .statusCode(statusCode);
    }

    @Then("the response should contain payment status {string}")
    public void verifyPaymentStatus(String status) {

        PaymentResponse paymentResponse =
                scenarioContext.getResponse()
                        .as(PaymentResponse.class);

        assertEquals(
                status,
                paymentResponse.getStatus(),
                "Payment status mismatch"
        );

        scenarioContext.getResponse()
                .then()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/payment-response-schema.json"
                        )
                );
    }

    @When("I send a payment request with invalid token")
    public void sendPaymentRequestWithInvalidToken() {

        PaymentRequest paymentRequest =
                new PaymentRequest("M123", 100, "USD");

        Response response =
                paymentApiClient.createPaymentWithInvalidToken(
                        paymentRequest
                );

        scenarioContext.setResponse(response);

        log.info("Response Status: {}", response.statusCode());
        log.info("Response Body: {}", response.asPrettyString());
    }

    @When("I send a payment request without token")
    public void sendPaymentRequestWithoutToken() {

        PaymentRequest paymentRequest =
                new PaymentRequest("M123", 100, "USD");

        Response response =
                paymentApiClient.createPaymentWithoutToken(
                        paymentRequest
                );

        scenarioContext.setResponse(response);

        log.info("Response Status: {}", response.statusCode());
        log.info("Response Body: {}", response.asPrettyString());
    }

    @When("I send a declined payment request")
    public void sendDeclinedPaymentRequest() {

        PaymentRequest paymentRequest =
                new PaymentTestDataBuilder()
                        .withMerchantId("M123")
                        .withAmount(100)
                        .withCurrency("USD")
                        .build();

        Response response =
                paymentApiClient.createPayment(paymentRequest);

        scenarioContext.setResponse(response);

        log.info(
                "Declined Payment Response Status: {}",
                response.statusCode()
        );

        log.info(
                "Response Body: {}",
                response.asPrettyString()
        );
    }

    @Then("the provider payment request should be sent")
    public void verifyProviderPaymentRequest() {

        WireMockManager.verifyPaymentRequest(
                merchantId,
                amount,
                currency
        );
    }

    @Then("the response should contain error message {string}")
    public void verifyErrorMessage(String expectedMessage) {

        scenarioContext.getResponse()
                .then()
                .body(
                        "message",
                        equalTo(expectedMessage)
                );
    }

    @Then("the error response should match the error schema")
    public void verifyErrorResponseSchema() {

        scenarioContext.getResponse()
                .then()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/error-response-schema.json"
                        )
                );
    }

    @Then("the provider payment request should not be sent")
    public void verifyProviderPaymentRequestNotSent() {

        WireMockManager.verifyProviderNotCalled();
    }
}