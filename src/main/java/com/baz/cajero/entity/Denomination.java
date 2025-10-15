package com.baz.cajero.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "denomination")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Denomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "initial_quantity", nullable = false)
    private Integer initialQuantity;

    public BigDecimal getValue() { return value; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }



}
