package com.mindhubap.homebanking.dtos;

import com.mindhubap.homebanking.enums.AccountType;
import com.mindhubap.homebanking.models.Account;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;

    private String number;

    private LocalDate creationDate;

    private double balance;

    private AccountType type;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        balance = account.getBalance();
        type = account.getType();
        transactions = account.getTransactions()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
