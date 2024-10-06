package com.survivorsLabs.paymentService.antiFraud;

import com.survivorsLabs.paymentService.client.IProviderFactory;
import com.survivorsLabs.paymentService.model.Payer;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = false)
public class AntiFraudFactory extends IProviderFactory<AntiFraudProperties> {

    protected AntiFraudFactory(AntiFraudProperties antiFraudProperties) {
        super(antiFraudProperties);
    }

    AntiFraudDTO toDTO(Payer payer){
        return getProviderProperties().getAntiFraudDTO().toBuilder()
                .contactPhone(payer.getContactPhone())
                .emailAddress(payer.getEmailAddress())
                .fullName(payer.getFullName())
                .dniNumber(payer.getDniNumber())
                .build();
    }
}
