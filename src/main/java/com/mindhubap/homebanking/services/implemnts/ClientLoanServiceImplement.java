package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.ClientLoanDTO;
import com.mindhubap.homebanking.models.ClientLoan;
import com.mindhubap.homebanking.repositories.ClientLoanRepository;
import com.mindhubap.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientLoanServiceImplement implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoan> findAll() {
        return clientLoanRepository.findAll();
    }

    @Override
    public List<ClientLoanDTO> convertToClientLoadDTO(List<ClientLoan> clientLoans) {
        return clientLoans.stream().map(ClientLoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public ClientLoan findById(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
