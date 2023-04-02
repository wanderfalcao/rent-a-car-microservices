package br.com.infnet.wander.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class AuthenticationResponse {
    
    @NotNull
    @Schema(name = "token", required = true)
    @JsonProperty("token")
    private final String token;
}
