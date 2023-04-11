package br.com.infnet.wander.domain.dto;


import br.com.infnet.wander.model.CarStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.*;

@AllArgsConstructor
@Data
public class CarRequest {
    @Valid
    @Schema(name = "car_status", required = true, example = "AVAILABLE")
    @JsonProperty("car_status")
    private CarStatus carStatus;

    @Valid
    @Size(max = 32)
    @Schema(name = "manufacturer", required = true, example = "Audi")
    @JsonProperty("manufacturer")
    private String manufacturer;

    @Valid
    @Size(max = 32)
    @Schema(name = "color", required = true, example = "Black")
    @JsonProperty("color")
    private String color;

    @Valid
    @Size(max = 32)
    @Schema(name = "model", required = true, example = "TT")
    @JsonProperty("model")
    private String model;

    
    @Valid
    @DecimalMin("0")
    @DecimalMax("9999999")
    @Schema(name = "price", required = true, example = "65000.00")
    @JsonProperty("price")
    private Double price;
    
    @Valid
    @Schema(name = "picture_link", required = true,
            example = "https://3.bp.blogspot.com/-o5ikK8MaxDY/WnNDQYU9UQI/AAAAAAABBqE/N0ZDRy1hmXIsv23QjtE8qF0zoh50J9u0wCLcBGAs/s1600/Audi-TT-RS-2019-Brasil%2B%25283%2529.jpg")
    @JsonProperty("picture_link")
    private String pictureLink;


}
