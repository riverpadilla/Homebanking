package com.mindhubap.homebanking.dtos;


import com.mindhubap.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {


    private long loanId;

    private String name;

    private double maxAmount;

    private List<Integer> payments;

    public LoanDTO(Loan loan) {
        loanId = loan.getId();
        name = loan.getName();
        maxAmount = loan.getMaxAmount();
        payments = loan.getPayments();
    }

    public long getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
