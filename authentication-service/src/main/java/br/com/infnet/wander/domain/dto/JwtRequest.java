package br.com.infnet.wander.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class JwtRequest {

    @JsonProperty("jwt")
    private final String jwt;

}
