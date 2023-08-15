package org.example.bank;

import lombok.Getter;
import lombok.Setter;
import org.example.utility.Helpers;

import java.util.List;

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

}
