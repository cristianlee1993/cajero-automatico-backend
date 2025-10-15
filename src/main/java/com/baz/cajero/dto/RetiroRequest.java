package com.baz.cajero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RetiroRequest {
    private BigDecimal monto;
    public BigDecimal getMonto() { return monto; }
}
