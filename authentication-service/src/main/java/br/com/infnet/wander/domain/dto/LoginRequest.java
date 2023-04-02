package br.com.infnet.wander.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class LoginRequest {

    @JsonProperty("email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Schema(name = "email", type = "email", example = "admin@admin.io", required = true)
    private final String email;

    @JsonProperty("password")
    @Schema(name = "password", example = "admin123", required = true)
    private final String password;
}
