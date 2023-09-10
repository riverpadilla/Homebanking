package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.ClientLoanDTO;
import com.mindhubap.homebanking.models.ClientLoan;

import java.util.List;
import java.util.Set;

public interface ClientLoanService {

    List<ClientLoan> findAll();

    List<ClientLoanDTO> convertToClientLoadDTO(List<ClientLoan> clientLoans);

    ClientLoan findById(Long id);

    void saveClientLoan(ClientLoan clientLoan);
}
