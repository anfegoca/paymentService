package com.survivorsLabs.muscleCoach.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
@NoArgsConstructor
public class ResponseCommand {

    @Builder.Default
    @Schema(example = "200", description = "Código de estado del consumo del servicio")
    private String code = String.valueOf(HttpStatus.OK.value());

    @Builder.Default
    @Schema(example = "Success", description = "Mensaje de estado del consumo del servicio")
    private String message = "Success";
}
