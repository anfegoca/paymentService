package com.survivorsLabs.paymentService.antiFraud;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.survivorsLabs.paymentService.client.IModel;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AntiFraudDTO extends IModel {

    private String code;
    private String message;
    private String description;
    private String fullName;
    private String emailAddress;
    private String contactPhone;
    private String dniNumber;
    private String client;

}
