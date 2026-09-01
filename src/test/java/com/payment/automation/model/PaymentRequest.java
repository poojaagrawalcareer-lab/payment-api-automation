package com.payment.automation.model;

public class PaymentRequest {

    private String merchantId;
    private int amount;
    private String currency;

    public PaymentRequest() {
    }

    public PaymentRequest(String merchantId, int amount, String currency) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}