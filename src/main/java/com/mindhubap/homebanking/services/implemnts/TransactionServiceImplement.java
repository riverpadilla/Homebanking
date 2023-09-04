package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.repositories.TransactionRepository;
import com.mindhubap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
