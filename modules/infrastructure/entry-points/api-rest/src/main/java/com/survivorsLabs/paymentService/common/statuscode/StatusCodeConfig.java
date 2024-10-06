package com.survivorsLabs.muscleCoach.common.statuscode;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Constants;
import org.springframework.beans.factory.annotation.Value;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "status-codes")
public class StatusCodeConfig implements Serializable {

    @Value("${:#{null}}")
    private Map<String, StatusCodeConfigItem> success;
    @Value("${:#{null}}")
    private Map<String, StatusCodeConfigItem> warns;
    @Value("${:#{null}}")
    private Map<String, StatusCodeConfigItem> fails;

    /**
     * Obtiene el mensaje homologado del código
     *
     * @throws Constants.ConstantException si el código no tiene homologación
     */
    public StatusCode resolve(String code) {
        try {
            return resolve(code, StatusCode.Level.SUCCESS);
        } catch (Constants.ConstantException eSuccess) {
            try {
                return resolve(code, StatusCode.Level.FAIL);
            } catch (Constants.ConstantException eFail) {
                return resolve(code, StatusCode.Level.WARN);
            }
        }
    }

    /**
     * Obtiene el mensaje homologado del código, solo del grupo del nivel asignado
     *
     * @throws Constants.ConstantException si el código no tiene homologación
     */
    public StatusCode resolve(String code, StatusCode.Level level) {
        Map<String, StatusCodeConfigItem> byLevel;
        switch (level) {
            case SUCCESS:
                byLevel = success;
                break;
            case WARN:
                byLevel = warns;
                break;
            default:
                byLevel = fails;
                break;
        }

        if (null != byLevel) {
            for (Map.Entry<String, StatusCodeConfigItem> entry : byLevel.entrySet()) {
                if (entry.getValue().containCode(code)) {
                    return entry.getValue().getStatusCode(level, entry.getKey(), code);
                }
            }
        }

        throw new Constants.ConstantException(StatusCodeConfig.class.getSimpleName(), code,
                "does not present homologation");
    }

    /**
     * Obtiene el mensaje homologado del código, sí no se encuentra configurado retorna una nueva instancia con el
     * nivel FAIL y el mensaje por defecto de FAIL
     */
    public StatusCode of(String code) {
        try {
            return resolve(code);
        } catch (Constants.ConstantException ce) {
            return new StatusCode(StatusCode.Level.FAIL, StatusCode.Level.FAIL.value(), ce.getMessage(), code);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusCodeConfigItem implements Serializable {

        private String message;
        @Value("#{'${}'.split(',')}")
        private List<String[]> codes;

        private boolean containCode(String code) {
            return codes.stream().anyMatch(s -> s[0].equals(code));
        }

        public StatusCode getStatusCode(StatusCode.Level level, String code, String extCode) {

            String msg = codes.stream().filter(detail -> detail[0].equals(extCode)).findFirst()
                    .map(detail -> detail.length > 1 ? String.join(", ",
                            Arrays.copyOfRange(detail, 1, detail.length)) : message)
                    .orElse(message);

            return new StatusCode(level, code, msg, extCode);
        }
    }

}