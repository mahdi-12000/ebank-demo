package com.mahdi.ebank.service;

import com.mahdi.ebank.exception.InsufficientBalanceException;
import com.mahdi.ebank.model.Account;
import com.mahdi.ebank.model.Transaction;

import java.math.BigDecimal;

/**
 * Test simple (sans frameworks) pour vérifier qu'un virement échoue
 * lorsque le compte source a un solde insuffisant.
 *
 * Utilise des vérifications explicites (if/throw) pour garantir l'exécution
 * même sans -ea (assertions JVM).
 *
 * Exécution recommandée :
 *   javac -d out $(find src -name "*.java")
 *   java -cp out com.mahdi.ebank.service.TransferServiceTest
 */
public class TransferServiceTest {

    public static void main(String[] args) {
        try {
            testInsufficientBalance();
            System.out.println("TEST PASSED: testInsufficientBalance");
        } catch (Throwable t) {
            System.err.println("TEST FAILED: " + t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void testInsufficientBalance() throws Exception {
        // Arrange
        Account source = new Account(1L, new BigDecimal("100.00"));
        Account target = new Account(2L, new BigDecimal("50.00"));
        TransferService service = new TransferService();
        BigDecimal amount = new BigDecimal("300.00"); // plus que le solde du source

        BigDecimal sourceBefore = source.getBalance();
        BigDecimal targetBefore = target.getBalance();

        // Act
        boolean exceptionThrown = false;
        try {
            Transaction tx = service.executeTransfer(source, target, amount);
            // Si nous arrivons ici, le transfert a été effectué => échec du test
            throw new AssertionError("Le transfert a été effectué alors qu'il aurait dû échouer : " + tx);
        } catch (InsufficientBalanceException expected) {
            exceptionThrown = true;
            // expected — test continue pour vérifier l'état des comptes
        }

        // Assert
        if (!exceptionThrown) {
            throw new AssertionError("InsufficientBalanceException attendue mais non levée");
        }
        if (source.getBalance() == null || target.getBalance() == null) {
            throw new AssertionError("Un des soldes est null après l'opération");
        }
        if (source.getBalance().compareTo(sourceBefore) != 0) {
            throw new AssertionError("Le solde du compte source a changé après un virement échoué");
        }
        if (target.getBalance().compareTo(targetBefore) != 0) {
            throw new AssertionError("Le solde du compte cible a changé après un virement échoué");
        }
    }
}
