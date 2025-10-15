package com.baz.cajero.controller;

import com.baz.cajero.entity.Denomination;
import com.baz.cajero.dto.RetiroRequest;
import com.baz.cajero.dto.RetiroResponse;
import com.baz.cajero.service.CajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cajero")
@CrossOrigin(origins = "http://localhost:4200")
public class CajeroController {

    @Autowired
    private CajeroService cajeroService;

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldoTotal() {
        BigDecimal saldo = cajeroService.getSaldoTotal();
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/denominaciones")
    public ResponseEntity<List<Denomination>> getDenominaciones() {
        List<Denomination> denominaciones = cajeroService.getDenominaciones();
        return ResponseEntity.ok(denominaciones);
    }

    @PostMapping("/retirar")
    public ResponseEntity<?> retirar(@RequestBody RetiroRequest request) {
        try {
            // 🔹 Validación básica del cuerpo
            if (request == null) {
                return ResponseEntity.badRequest().body(
                        crearRespuestaError("La solicitud no puede ser nula")
                );
            }

            if (request.getMonto() == null) {
                return ResponseEntity.badRequest().body(
                        crearRespuestaError("El monto no puede ser nulo")
                );
            }

            BigDecimal monto = request.getMonto();

            // 🔹 Validar monto mayor que 0
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body(
                        crearRespuestaError("El monto debe ser mayor a cero")
                );
            }

            // 🔹 Normalizar el monto para evitar problemas con escala
            monto = monto.setScale(2, BigDecimal.ROUND_HALF_UP);

            // 🔹 Validación del formato decimal (.00, .5 o .50)
            BigDecimal decimalPart = monto.remainder(BigDecimal.ONE);
            boolean esDecimalValido =
                    decimalPart.compareTo(BigDecimal.ZERO) == 0 ||
                            decimalPart.compareTo(new BigDecimal("0.5")) == 0 ||
                            decimalPart.compareTo(new BigDecimal("0.50")) == 0;

            if (!esDecimalValido) {
                return ResponseEntity.badRequest().body(
                        crearRespuestaError("Solo se permiten montos con decimales .00 o .50")
                );
            }

            // 🔹 Procesar el retiro
            RetiroResponse response = cajeroService.procesarRetiro(request);

            // 🔹 Validar respuesta del servicio
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error interno del servidor: " + e.getMessage()));
        }
    }

    // 🔹 Método auxiliar para construir mensajes de error
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("exitoso", false);
        errorResponse.put("mensaje", mensaje);
        errorResponse.put("timestamp", LocalDateTime.now());
        return errorResponse;
    }
}
