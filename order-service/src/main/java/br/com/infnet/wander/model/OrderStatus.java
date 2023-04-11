package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString
public enum OrderStatus {
    CREATED, CANCELED, ACTIVE, RETURNED;

    @JsonValue
    public String getValue() {
        return this.name();
    }

}
