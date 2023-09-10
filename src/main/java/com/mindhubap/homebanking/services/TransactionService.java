package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.TransactionDTO;
import com.mindhubap.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAllTransactions();

    List<TransactionDTO> convertToTransactionDTO(List<Transaction> transactions);

    Transaction findById(Long id);

    void saveTransaction(Transaction transaction);
}
