package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.models.ClientLoan;
import com.mindhubap.homebanking.repositories.ClientLoanRepository;
import com.mindhubap.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
