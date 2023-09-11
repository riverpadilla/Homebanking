package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.LoanDTO;
import com.mindhubap.homebanking.models.Loan;
import com.mindhubap.homebanking.repositories.LoanRepository;
import com.mindhubap.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Set<LoanDTO> convertToLoanDTO(List<Loan> loans) {
        return loans.stream().map(LoanDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Loan findByName(String name) {
        return loanRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return loanRepository.existsByName(name);
    }

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }
}
