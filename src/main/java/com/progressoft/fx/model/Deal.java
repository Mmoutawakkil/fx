package com.progressoft.fx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deals", indexes = {}, uniqueConstraints = {@UniqueConstraint(columnNames = {"deal_unique_id"})})
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "deal_unique_id", nullable = false, length = 128, unique = true)
    private String dealUniqueId;
    @Column(name = "from_currency", nullable = false, length = 3)
    private String fromCurrency;
    @Column(name = "to_currency", nullable = false, length = 3)
    private String toCurrency;
    @Column(name = "deal_timestamp", nullable = false)
    private OffsetDateTime dealTimestamp;
    @Column(name = "amount", nullable = false, precision = 20, scale = 6)
    private BigDecimal amount;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now(); // current timestamp
    }
}
