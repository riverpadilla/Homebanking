package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.LoanDTO;
import com.mindhubap.homebanking.models.Loan;

import java.util.List;
import java.util.Set;

public interface LoanService {

    List<Loan> findAll();

    Set<LoanDTO> convertToLoanDTO(List<Loan> loans);

    Loan findById(Long id);

    void saveLoan(Loan loan);
}