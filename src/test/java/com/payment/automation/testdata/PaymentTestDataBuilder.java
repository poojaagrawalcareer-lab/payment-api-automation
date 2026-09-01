package com.payment.automation.testdata;

import com.payment.automation.model.PaymentRequest;

public class PaymentTestDataBuilder {

    private String merchantId = "M123";
    private int amount = 100;
    private String currency = "USD";

    public PaymentTestDataBuilder withMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PaymentTestDataBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public PaymentTestDataBuilder withCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public PaymentRequest build() {
        return new PaymentRequest(
                merchantId,
                amount,
                currency
        );
    }
}