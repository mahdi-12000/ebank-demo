package com.mahdi.ebank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Représente une transaction entre deux comptes.
 */
public class Transaction {

    private Long id;
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    /**
     * Constructeur par défaut.
     */
    public Transaction() {
    }

    /**
     * Constructeur complet (avec id et timestamp).
     */
    public Transaction(Long id, Long sourceAccountId, Long targetAccountId, BigDecimal amount, LocalDateTime timestamp) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * Constructeur pour créer une nouvelle transaction (id généré ultérieurement, timestamp = now).
     */
    public Transaction(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        this(null, sourceAccountId, targetAccountId, amount, LocalDateTime.now());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Transaction{" +
                "id=" + id +
                ", sourceAccountId=" + sourceAccountId +
                ", targetAccountId=" + targetAccountId +
                ", amount=" + (amount == null ? "null" : amount.toPlainString()) +
                ", timestamp=" + (timestamp == null ? "null" : timestamp.format(fmt)) +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceAccountId, targetAccountId, amount, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction)) return false;
        Transaction other = (Transaction) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(sourceAccountId, other.sourceAccountId)
                && Objects.equals(targetAccountId, other.targetAccountId)
                && Objects.equals(amount, other.amount)
                && Objects.equals(timestamp, other.timestamp);
    }
}
