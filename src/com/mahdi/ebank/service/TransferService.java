package mahdi.ebank.service;

import com.mahdi.ebank.exception.InsufficientBalanceException;
import com.mahdi.ebank.model.Account;
import com.mahdi.ebank.model.Transaction;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Service simple pour effectuer des virements entre comptes.
 *
 * Remarques:
 * - Cette implémentation suppose que la classe Account fournit au minimum:
 *     Long getId();
 *     BigDecimal getBalance();
 *     void setBalance(BigDecimal newBalance);
 *
 *   Si votre Account expose des méthodes withdraw/deposit, adaptez les appels en conséquence.
 *
 * - La méthode est thread-safe et évite les risques de deadlock en ordonnant les verrous
 *   selon l'ID du compte (ou l'identityHashCode si les IDs sont null).
 */
public class TransferService {

    /**
     * Exécute un virement simple : vérifie le solde, débite la source, crédite la cible,
     * et retourne la Transaction correspondante.
     *
     * @param source compte source (non null)
     * @param target compte cible (non null)
     * @param amount montant à transférer (> 0)
     * @return l'objet Transaction représentant le virement
     * @throws InsufficientBalanceException si le compte source n'a pas assez de fonds
     * @throws IllegalArgumentException     si les paramètres sont invalides
     */
    public Transaction executeTransfer(Account source, Account target, BigDecimal amount)
            throws InsufficientBalanceException {

        Objects.requireNonNull(source, "source account must not be null");
        Objects.requireNonNull(target, "target account must not be null");
        Objects.requireNonNull(amount, "amount must not be null");

        if (source == target) {
            throw new IllegalArgumentException("source and target accounts must be different");
        }

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }

        // Determine lock ordering to avoid deadlocks
        final Object lock1;
        final Object lock2;

        Long sId = null;
        Long tId = null;
        try {
            sId = source.getId();
            tId = target.getId();
        } catch (Exception ignored) {
            // If getId() throws or is not available, we fall back to identityHashCode ordering below.
        }

        if (sId != null && tId != null) {
            if (sId.compareTo(tId) < 0) {
                lock1 = source;
                lock2 = target;
            } else {
                lock1 = target;
                lock2 = source;
            }
        } else {
            // Fallback if IDs are null
            int h1 = System.identityHashCode(source);
            int h2 = System.identityHashCode(target);
            if (h1 < h2) {
                lock1 = source;
                lock2 = target;
            } else {
                lock1 = target;
                lock2 = source;
            }
        }

        synchronized (lock1) {
            synchronized (lock2) {
                BigDecimal sourceBalance = source.getBalance();
                if (sourceBalance == null) {
                    throw new IllegalStateException("source account balance is null");
                }

                if (sourceBalance.compareTo(amount) < 0) {
                    throw new InsufficientBalanceException("Solde insuffisant dans le compte source (id="
                            + source.getId() + "). Solde: " + sourceBalance + ", Montant demandé: " + amount);
                }

                // Mise à jour des soldes
                BigDecimal newSourceBalance = sourceBalance.subtract(amount);
                BigDecimal targetBalance = target.getBalance();
                if (targetBalance == null) {
                    targetBalance = BigDecimal.ZERO;
                }
                BigDecimal newTargetBalance = targetBalance.add(amount);

                source.setBalance(newSourceBalance);
                target.setBalance(newTargetBalance);

                // Crée et retourne la transaction (id laissé à null / généré ailleurs si nécessaire)
                return new Transaction(source.getId(), target.getId(), amount);
            }
        }
    }
}