package com.baz.cajero.service;

import com.baz.cajero.dto.RetiroRequest;
import com.baz.cajero.dto.RetiroResponse;
import com.baz.cajero.entity.Denomination;
import com.baz.cajero.entity.Transaction;
import com.baz.cajero.repository.DenominationRepository;
import com.baz.cajero.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CajeroService {

    @Autowired
    private DenominationRepository denominationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BigDecimal getSaldoTotal() {
        BigDecimal saldo = denominationRepository.getTotalBalance();
        return saldo != null ? saldo : BigDecimal.ZERO;
    }

    public List<Denomination> getDenominaciones() {
        return denominationRepository.findAllByOrderByValueDesc();
    }

    @Transactional
    public RetiroResponse procesarRetiro(RetiroRequest request) {
        try {
            BigDecimal monto = request.getMonto();

            // Validar que el monto no sea nulo
            if (monto == null) {
                return crearTransaccionFallida(BigDecimal.ZERO, "El monto no puede ser nulo");
            }

            // Validar que sea mayor a cero
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                return crearTransaccionFallida(monto, "El monto debe ser mayor a cero");
            }

            // Validar montos muy pequeños
            if (monto.compareTo(new BigDecimal("0.50")) < 0) {
                return crearTransaccionFallida(monto, "El monto mínimo de retiro es $0.50");
            }

            // 🔹 CORREGIDO: Validación que acepta tanto .5 como .50
            BigDecimal decimalPart = monto.remainder(BigDecimal.ONE);

            // Crear valores de comparación
            BigDecimal cero = BigDecimal.ZERO;
            BigDecimal medioConUnDecimal = new BigDecimal("0.5");
            BigDecimal medioConDosDecimales = new BigDecimal("0.50");

            // Validar contra ambos valores (0.5 y 0.50)
            boolean esDecimalValido = decimalPart.compareTo(cero) == 0 ||
                    decimalPart.compareTo(medioConUnDecimal) == 0 ||
                    decimalPart.compareTo(medioConDosDecimales) == 0;

            if (!esDecimalValido) {
                return crearTransaccionFallida(monto, "Solo se permiten montos con decimales .00 o .50");
            }

            // Validar fondos suficientes
            BigDecimal saldoTotal = getSaldoTotal();
            if (monto.compareTo(saldoTotal) > 0) {
                return crearTransaccionFallida(monto,
                        String.format("Fondos insuficientes. Saldo disponible: $%.2f", saldoTotal));
            }

            // Calcular denominaciones
            Map<BigDecimal, Integer> denominaciones = calcularDenominaciones(monto);

            if (denominaciones == null || denominaciones.isEmpty()) {
                return crearTransaccionFallida(monto,
                        "No se pueden dispensar las denominaciones solicitadas con el inventario actual");
            }

            // Validar que las denominaciones sumen exactamente el monto
            BigDecimal totalCalculado = denominaciones.entrySet().stream()
                    .map(entry -> entry.getKey().multiply(BigDecimal.valueOf(entry.getValue())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalCalculado.compareTo(monto) != 0) {
                return crearTransaccionFallida(monto, "Error en el cálculo de denominaciones");
            }

            // Actualizar inventario
            actualizarInventario(denominaciones);

            // Crear respuesta exitosa
            return crearTransaccionExitosa(monto, denominaciones);

        } catch (Exception e) {
            // Capturar cualquier error inesperado
            BigDecimal monto = request != null ? request.getMonto() : BigDecimal.ZERO;
            return crearTransaccionFallida(monto,
                    "Error interno del sistema: " + e.getMessage());
        }
    }


    private Map<BigDecimal, Integer> calcularDenominaciones(BigDecimal monto) {
        List<Denomination> disponibles = denominationRepository.findAllByOrderByValueDesc();
        Map<BigDecimal, Integer> resultado = new HashMap<>();
        BigDecimal restante = monto;

        for (Denomination denom : disponibles) {
            if (restante.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

            if (restante.compareTo(denom.getValue()) >= 0 && denom.getQuantity() > 0) {
                int maxUsar = Math.min(
                        denom.getQuantity(),
                        restante.divide(denom.getValue(), 0, RoundingMode.FLOOR).intValue()
                );

                if (maxUsar > 0) {
                    resultado.put(denom.getValue(), maxUsar);
                    restante = restante.subtract(denom.getValue().multiply(BigDecimal.valueOf(maxUsar)));
                }
            }
        }

        // Redondear para evitar problemas de precisión con decimales
        restante = restante.setScale(2, RoundingMode.HALF_UP);

        return restante.compareTo(BigDecimal.ZERO) == 0 ? resultado : null;
    }

    private void actualizarInventario(Map<BigDecimal, Integer> denominaciones) {
        for (Map.Entry<BigDecimal, Integer> entry : denominaciones.entrySet()) {
            Denomination denom = denominationRepository.findByValue(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Denominación no encontrada: " + entry.getKey()));

            denom.setQuantity(denom.getQuantity() - entry.getValue());
            denominationRepository.save(denom);
        }
    }

    private RetiroResponse crearTransaccionExitosa(BigDecimal monto, Map<BigDecimal, Integer> denominaciones) {
        try {
            String denominacionesJson = objectMapper.writeValueAsString(denominaciones);
            Transaction transaccion = new Transaction(monto, true, denominacionesJson, null);
            transactionRepository.save(transaccion);

            BigDecimal saldoRestante = getSaldoTotal();

            return new RetiroResponse(
                    true,
                    "Retiro exitoso",
                    denominaciones,
                    saldoRestante
            );

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar las denominaciones", e);
        }
    }

    private RetiroResponse crearTransaccionFallida(BigDecimal monto, String mensajeError) {
        Transaction transaccion = new Transaction(monto, false, null, mensajeError);
        transactionRepository.save(transaccion);

        return new RetiroResponse(
                false,
                mensajeError,
                null,
                getSaldoTotal()
        );
    }
}
