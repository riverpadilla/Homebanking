package com.mindhubap.homebanking.dtos;

import com.mindhubap.homebanking.models.Account;
import java.time.LocalDate;

public class AccountDTO {

    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;

    public AccountDTO(Account account) {
        id = account.getId();
        number = account.getNumber();
        creationDate = account.getCreationDate();
        balance = account.getBalance();
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


}
