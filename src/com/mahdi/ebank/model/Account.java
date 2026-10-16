package mahdi.ebank.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Représente un compte simple avec id et solde.
 * API minimale attendue par TransferService : getId(), getBalance(), setBalance().
 */
public class Account {

    private final Long id;
    private BigDecimal balance;

    public Account(Long id, BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.balance = balance == null ? BigDecimal.ZERO : balance;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", balance=" + (balance == null ? "null" : balance.toPlainString()) + "}";
    }
}