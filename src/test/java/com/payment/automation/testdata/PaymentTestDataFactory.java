package com.payment.automation.testdata;

import com.payment.automation.model.PaymentRequest;

public class PaymentTestDataFactory {

    public static PaymentRequest createPayment(
            String merchantId,
            int amount,
            String currency) {

        return new PaymentTestDataBuilder()
                .withMerchantId(merchantId)
                .withAmount(amount)
                .withCurrency(currency)
                .build();
    }

}