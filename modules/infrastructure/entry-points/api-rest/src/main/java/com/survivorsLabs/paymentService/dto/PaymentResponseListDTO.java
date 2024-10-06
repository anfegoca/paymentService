package com.survivorsLabs.paymentService.dto;

import com.survivorsLabs.paymentService.common.ResponseCommand;
import com.survivorsLabs.paymentService.model.TransactionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PaymentResponseDTO extends ResponseCommand {
    private TransactionResponse transactionResponse;
}
