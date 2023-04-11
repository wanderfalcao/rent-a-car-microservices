package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@Document
@NoArgsConstructor
public class Car {
    @Transient
    public static final String SEQUENCE_NAME = "car_sequence";

    @Id
    @Valid
    @Schema(name = "car_id", required = true, example = "1")
    @JsonProperty("car_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @Schema(name = "car_status", required = true, example = "AVAILABLE")
    @JsonProperty("car_status")
    private CarStatus carStatus;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 32)
    @Schema(name = "manufacturer", required = true, example = "Audi")
    @JsonProperty("manufacturer")
    private String manufacturer;

    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 32)
    @Schema(name = "color", required = true, example = "Black")
    @JsonProperty("color")
    private String color;
    
    @Pattern(regexp = "^([\\w. ]+)$")
    @Size(max = 32)
    @Schema(name = "model", required = true, example = "TT")
    @JsonProperty("model")
    private String model;
    
    @Valid
    @DecimalMin("0")
    @DecimalMax("999999")
    @Schema(name = "price", required = true, example = "65000.00")
    @JsonProperty("price")
    private Double price;
    @Valid
    @Schema(name = "picture_link", required = true,
            example = "https://3.bp.blogspot.com/-o5ikK8MaxDY/WnNDQYU9UQI/AAAAAAABBqE/N0ZDRy1hmXIsv23QjtE8qF0zoh50J9u0wCLcBGAs/s1600/Audi-TT-RS-2019-Brasil%2B%25283%2529.jpg")
    @JsonProperty("picture_link")
    private String pictureLink;

}
