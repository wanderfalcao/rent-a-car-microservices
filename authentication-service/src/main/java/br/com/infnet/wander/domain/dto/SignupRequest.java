package br.com.infnet.wander.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class SignupRequest {
    @JsonProperty("email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Schema(name = "email", type = "email", example = "admin@admin.io", required = true)
    private final String email;

    @JsonProperty("firstname")
    @Schema(name = "firstname", example = "wander", required = true)
    private final String firstname;

    @JsonProperty("lastname")
    @Schema(name = "lastname", example = "campelo", required = true)
    private final String lastname;

    @JsonProperty("password")
    @Schema(name = "password", example = "W123@xji", required = true)
    private final String password;

    @JsonProperty("Role")
    @Schema(name = "Role", example = "user", required = false)
    private final String role;
}
