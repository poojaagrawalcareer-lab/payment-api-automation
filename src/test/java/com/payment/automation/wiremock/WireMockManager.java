package com.payment.automation.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.payment.automation.config.ConfigManager;
import com.payment.automation.steps.UserApiSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockManager {
    private static final Logger log =
            LoggerFactory.getLogger(WireMockManager.class);

    private static WireMockServer wireMockServer;

    public static void startServer() {

        int port = Integer.parseInt(
                ConfigManager.get("wiremock.port")
        );

        wireMockServer = new WireMockServer(port);
        wireMockServer.start();

        configureFor("localhost", port);

        log.info("WireMock started on port 8089");
    }

    public static void stopServer() {

        if (wireMockServer != null) {
            wireMockServer.stop();
            log.info("WireMock stopped");
        }
    }

    public static void createPaymentApprovedStub() {

        wireMockServer.stubFor(
                post(urlEqualTo("/provider/payment"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBody("""
                                                {
                                                  "status": "APPROVED",
                                                  "transactionId": "TXN123"
                                                }
                                                """)
                        )
        );

        log.info("Payment APPROVED stub created");
    }

    public static void createPaymentDeclinedStub() {

        wireMockServer.stubFor(
                post(urlEqualTo("/provider/payment"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBody("""
                                            {
                                              "status": "DECLINED",
                                              "transactionId": "TXN456"
                                            }
                                            """)
                        )
        );

        log.info("Payment DECLINED stub created");
    }

    public static void verifyPaymentRequest(
            String merchantId,
            int amount,
            String currency) {

        wireMockServer.verify(
                1,
                postRequestedFor(urlEqualTo("/provider/payment"))
                        .withRequestBody(
                                matchingJsonPath(
                                        "$.merchantId",
                                        equalTo(merchantId)
                                )
                        )
                        .withRequestBody(
                                matchingJsonPath(
                                        "$.amount",
                                        equalTo(String.valueOf(amount))
                                )
                        )
                        .withRequestBody(
                                matchingJsonPath(
                                        "$.currency",
                                        equalTo(currency)
                                )
                        )
        );

        log.info(
                "Provider payment request verified: "
                        + merchantId + ", "
                        + amount + ", "
                        + currency
        );
    }

    public static void verifyProviderNotCalled() {

        wireMockServer.verify(
                0,
                postRequestedFor(urlEqualTo("/provider/payment"))
        );

        log.info("Provider was not called for invalid request");
    }
}