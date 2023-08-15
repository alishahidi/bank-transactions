package org.example.bank;

import lombok.Getter;
import lombok.Setter;
import org.example.utility.Helpers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Bank {
    private List<Account> accounts;
    private List<Transaction> transactions;

    public Bank() {
        List<String> accountLines = Helpers.readLines("/accounts.csv");
        accounts = accountLines.stream()
                .map(account -> account.split(",", 3))
                .map(account -> {
                    Integer id = Integer.parseInt(account[0]);
                    String name = account[1];
                    long amount = Long.parseLong(account[2].replaceAll(",", ""));
                    return new Account(id, name, amount, amount);
                })
                .toList();

        List<String> transactionLines = Helpers.readLines("/transactions.csv");
        transactions = transactionLines.stream()
                .map(account -> account.split(",", 3))
                .map(account -> {
                    Integer from = Integer.parseInt(account[0]);
                    Integer to = Integer.parseInt(account[1]);
                    long amount = Long.parseLong(account[2].replaceAll("\"", "").replaceAll(",", ""));
                    return new Transaction(from, to, amount);
                })
                .toList();
    }

    public void executeTransactions() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Transaction transaction : transactions) {
            executorService.submit(() -> processTransaction(transaction, accounts));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    public void showNetBalance() {
        for (Account account : accounts) {
            long netChange = account.getBalance() - account.getFirstBalance();
            System.out.println("Account " + account.getId() + " amount is: " + netChange);
        }
    }

    private static void processTransaction(Transaction transaction, List<Account> accounts) {
        Account fromAccount = findAccountById(accounts, transaction.from());
        Account toAccount = findAccountById(accounts, transaction.to());
        synchronized (fromAccount) {
            fromAccount.setBalance(fromAccount.getBalance() - transaction.amount());
        }
        synchronized (toAccount) {
            toAccount.setBalance(toAccount.getBalance() + transaction.amount());
        }
    }

    private static Account findAccountById(List<Account> accounts, Integer accountId) {
        return accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID: " + accountId));
    }

}
