package com.payment.automation.hooks;

import com.payment.automation.context.ScenarioContext;
import com.payment.automation.wiremock.WireMockManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class TestHooks {

    private final ScenarioContext scenarioContext;

    public TestHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before
    public void setUp(Scenario scenario) {

        WireMockManager.startServer();

        if (scenario.getSourceTagNames().contains("@declined")) {
            WireMockManager.createPaymentDeclinedStub();
        } else {
            WireMockManager.createPaymentApprovedStub();
        }
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()
                && scenarioContext.getResponse() != null) {

            String responseBody =
                    scenarioContext.getResponse().asPrettyString();

            scenario.log("API Response:");
            scenario.log(responseBody);

        }

        WireMockManager.stopServer();
    }
}