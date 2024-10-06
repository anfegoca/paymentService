package com.survivorsLabs.paymentService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payment {
    private Payer payer;
    private Card card;
    private BillingAddress billingAddress;
    private double value;
    private String currency;
}
