package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(	name = "OpeningHours")
@AllArgsConstructor
@Data
public class OpeningHours {
    
    @Valid
    @Schema(name = "opening_hours_id", required = true)
    @JsonProperty("opening_hours_id")
    private Integer openingHoursId;
    
    @Valid
    @Schema(name = "monday", required = true)
    @JsonProperty("monday")
    private String monday;
    
    @Valid
    @Schema(name = "tuesday", required = true)
    @JsonProperty("tuesday")
    private String tuesday;
    
    @Valid
    @Schema(name = "wednesday", required = true)
    @JsonProperty("wednesday")
    private String wednesday;
    
    @Valid
    @Schema(name = "thursday", required = true)
    @JsonProperty("thursday")
    private String thursday;
    
    @Valid
    @Schema(name = "friday", required = true)
    @JsonProperty("friday")
    private String friday;
    
    @Valid
    @Schema(name = "saturday", required = true)
    @JsonProperty("saturday")
    private String saturday;
    
    @Valid
    @Schema(name = "sunday", required = true)
    @JsonProperty("sunday")
    private String sunday;

}
