package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString
public enum CarStatus {
    REPAIR, AVAILABLE, RENTED;
    
    @JsonValue
    public String getValue() {
        return this.name();
    }
}
