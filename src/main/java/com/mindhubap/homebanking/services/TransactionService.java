package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.models.Transaction;

public interface TransactionService {

    void saveTransaction(Transaction transaction);
}
