package com.mindhubap.homebanking.dtos;

import com.mindhubap.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private Long id;

    private Long loanId;

    private long clientId;

    private String name;

    private double amount;

    private Integer payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        id = clientLoan.getId();
        loanId = clientLoan.getLoan().getId();
        clientId = clientLoan.getClient().getId();
        name = clientLoan.getLoan().getName();
        amount = clientLoan.getAmount();
        payments = clientLoan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public long getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }
}
