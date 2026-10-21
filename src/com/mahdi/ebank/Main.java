package com.mahdi.ebank;

import com.mahdi.ebank.model.Account;
import com.mahdi.ebank.model.Transaction;
import com.mahdi.ebank.service.TransferService;
import com.mahdi.ebank.exception.InsufficientBalanceException;

import java.math.BigDecimal;

/**
 * Programme principal démonstratif pour exécuter un virement entre deux comptes.
 */
public class Main {

    public static void main(String[] args) {
        // Création de deux comptes (id et solde initial)
        Account account1 = new Account(1L, new BigDecimal("1000.00"));
        Account account2 = new Account(2L, new BigDecimal("250.00"));
         
        System.out.println("Soldes avant virement :");
        System.out.println("Compte 1 (id=" + account1.getId() + ") : " + account1.getBalance());
        System.out.println("Compte 2 (id=" + account2.getId() + ") : " + account2.getBalance());

        TransferService transferService = new TransferService();
        BigDecimal montant = new BigDecimal("300.00");

        try {
            Transaction transaction = transferService.executeTransfer(account1, account2, montant);

            System.out.println("\nVirement effectué avec succès :");
            System.out.println(transaction);

        } catch (InsufficientBalanceException e) {
            System.err.println("\nÉchec du virement : solde insuffisant - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nÉchec inattendu du virement : " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\nSoldes après virement :");
        System.out.println("Compte 1 (id=" + account1.getId() + ") : " + account1.getBalance());
        System.out.println("Compte 2 (id=" + account2.getId() + ") : " + account2.getBalance());
    }
}
