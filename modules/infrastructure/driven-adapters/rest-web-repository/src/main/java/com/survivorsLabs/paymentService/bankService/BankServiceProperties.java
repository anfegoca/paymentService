package com.survivorsLabs.paymentService.antiFraud;

import com.survivorsLabs.paymentService.client.IProviderProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "providers.anti-fraud")
public class AntiFraudProperties extends IProviderProperties {
    private AntiFraudDTO antiFraudDTO;
}
