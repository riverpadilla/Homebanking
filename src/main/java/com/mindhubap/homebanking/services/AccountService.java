package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.models.Account;

import java.util.List;

public interface AccountService {


    List<Account> findAllAccounts();

    List<AccountDTO> convertToAccountsDTO(List<Account> accounts);

    Account findById(Long Id);

    boolean existsByNumber(String number);

    void saveAccount(Account account);

}
