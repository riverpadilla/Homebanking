package com.mindhubap.homebanking.dtos;

import com.mindhubap.homebanking.models.ClientLoan;

public class LoanApplicationDTO {

    private Long loanId;

    private double amount;

    private Integer payments;

    private String accountToNumber;

    public LoanApplicationDTO(Long loanId, double amount, Integer payments, String accountToNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.accountToNumber = accountToNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountToNumber() {
        return accountToNumber;
    }
}
