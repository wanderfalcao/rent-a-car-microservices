package br.com.infnet.wander.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.ToString;

@ToString
public enum CurrencySymbol {
    USD, JPY, BGN, CZK, DKK, GBP, HUF, PLN, RON, SEK, CHF, ISK, NOK, HRK, TRY, AUD, BRL, CAD, CNY, HKD, IDR, ILS, INR,
    KRW, MXN, MYR, NZD, PHP, SGD, THB, ZAR, EUR;
    
    @JsonCreator
    public static CurrencySymbol fromValue(String value) {
        for (CurrencySymbol b : CurrencySymbol.values()) {
            if (b.name().equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
    
    @JsonValue
    public String getValue() {
        return this.name();
    }
}