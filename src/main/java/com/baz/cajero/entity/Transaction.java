package com.baz.cajero.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "denominations", columnDefinition = "JSON")
    private String denominations;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    // Constructores
    public Transaction() {}

    public Transaction(BigDecimal amount, Boolean success, String denominations, String errorMessage) {
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.success = success;
        this.denominations = denominations;
        this.errorMessage = errorMessage;
    }

}
