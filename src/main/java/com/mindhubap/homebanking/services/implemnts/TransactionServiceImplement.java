package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.TransactionDTO;
import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.repositories.TransactionRepository;
import com.mindhubap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<TransactionDTO> convertToTransactionDTO(List<Transaction> transactions) {
        return transactions.stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
