package br.com.infnet.wander.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.math.BigInteger;

@Data
public class LoginOrderRequest {
    @JsonProperty("order_id")
    @Email
    @Schema(name = "order_id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
    private final BigInteger id;
    @JsonProperty("last_name")
    @Schema(name = "last_name", example = "Hilton", required = true)
    private final String lastName;
}
