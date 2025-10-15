package com.baz.cajero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RetiroResponse {
    private boolean success;
    private String message;
    private Map<BigDecimal, Integer> denominations;
    private BigDecimal saldoRestante;

    // Constructores
    public RetiroResponse() {}

    public RetiroResponse(boolean success, String message, Map<BigDecimal, Integer> denominations, BigDecimal saldoRestante) {
        this.success = success;
        this.message = message;
        this.denominations = denominations;
        this.saldoRestante = saldoRestante;
    }

}
