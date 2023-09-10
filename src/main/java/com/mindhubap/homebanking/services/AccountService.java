package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;

import java.util.List;

public interface AccountService {


    List<Account> findAllAccounts();

    List<AccountDTO> convertToAccountsDTO(List<Account> accounts);

    Account findById(Long Id);

    Account findByNumber(String number);

    boolean existsByNumber(String number);

    boolean existsByClientAndNumber(Client client, String number);

    void saveAccount(Account account);

    void deleteAccount(Account account);

}
