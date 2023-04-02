package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiError {
    
    @NotNull
    @Schema(name = "status", required = true)
    @JsonProperty("status")
    private final HttpStatus status;

    @NotNull
    @Schema(name = "message")
    @JsonProperty("message")
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", locale = "de_AT")
    private LocalDateTime timestamp;

    @NotNull
    @Schema(name = "errors")
    @JsonProperty("errors")
    private List<String> errors = new ArrayList<>();
    
    
    public ApiError(HttpStatus status, String message, String error) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
    
    public ApiError(HttpStatus status, String message) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }
    
}

