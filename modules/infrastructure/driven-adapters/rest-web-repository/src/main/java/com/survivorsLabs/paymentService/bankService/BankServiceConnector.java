package com.survivorsLabs.paymentService.antiFraud;

import com.survivorsLabs.paymentService.client.HttpRequest;
import com.survivorsLabs.paymentService.client.IProviderConnector;
import com.survivorsLabs.paymentService.client.ServiceException;
import com.survivorsLabs.paymentService.exceptions.AntiFraudExeption;
import com.survivorsLabs.paymentService.model.AntiFraudValidation;
import com.survivorsLabs.paymentService.model.Payer;
import com.survivorsLabs.paymentService.port.out.AntiFraudServiceOutPort;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = false)
public class AntiFraudConnector extends IProviderConnector<AntiFraudProperties, AntiFraudFactory> implements AntiFraudServiceOutPort {

    private static final String SUCCESS_CODE = "200";

    public AntiFraudConnector(@NotNull AntiFraudProperties properties,
                              @NotNull AntiFraudFactory factory,
                              @NotNull Environment environment) {
        super(properties, factory, environment);
    }

    @Override
    public AntiFraudValidation queryAntiFraud(Payer payer) throws AntiFraudExeption {
        try {
            AntiFraudDTO response = invoke(AntiFraudProcess.QUERY_PAYER.name(),
                    HttpRequest.builder().httpMethod(HttpMethod.POST).path(getProviderProperties().getUrl())
                            .body(getProviderFactory().toDTO(payer))
                            .build(), AntiFraudDTO.class,true);

            if (!SUCCESS_CODE.equals(response.getCode())) {
                throw new AntiFraudExeption(response.getMessage());
            }
            return AntiFraudValidation.builder()
                    .code(response.getCode())
                    .response(response.getMessage())
                    .description(response.getDescription())
                    .build();

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
