package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@Entity
@Table(	name = "locations")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Location {

    @Id
    @Valid
    @Schema(name = "location_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "location_id", nullable = false)
    @JsonProperty("location_id")
    private BigInteger locationId;
    
    @ManyToOne
    @JoinColumn(name = "opening_hours_id")
    @JsonProperty("opening_hours")
    private OpeningHours openingHours;
    
    @Size(max = 64)
    @Schema(name = "location_name")
    @JsonProperty("location_name")
    private String locationName;
    
    @Size(max = 32)
    @Schema(name = "street_name")
    @JsonProperty("street_name")
    private String streetName;
    
    @Size(max = 32)
    @Schema(name = "street_number")
    @JsonProperty("street_number")
    private String streetNumber;
    
    @Size(max = 32)
    @Schema(name = "city_name")
    @JsonProperty("city_name")
    private String cityName;

    @Size(max = 64)
    @Schema(name = "email")
    @JsonProperty("email")
    private String email;
    
    @Size(max = 32)
    @Schema(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Schema(name = "saga_status")
    @JsonProperty("saga_status")
    private SagaStatus status;
}

