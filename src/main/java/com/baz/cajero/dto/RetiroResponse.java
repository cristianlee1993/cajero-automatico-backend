package com.baz.cajero.dto;

import java.math.BigDecimal;
import java.util.Map;

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

    public boolean isSuccess() {
        return success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<BigDecimal, Integer> getDenominations() {
        return denominations;
    }

    public void setDenominations(Map<BigDecimal, Integer> denominations) {
        this.denominations = denominations;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(BigDecimal saldoRestante) {
        this.saldoRestante = saldoRestante;
    }
}