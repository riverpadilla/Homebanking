package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> convertToAccountsDTO(List<Account> accounts) {

        return accounts.stream().map(AccountDTO:: new).collect(Collectors.toList());
    }

    @Override
    public Account findById(Long Id) {
        return accountRepository.findById(Id).orElse(null);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public boolean existsByClientAndNumber(Client client, String number) {
        return accountRepository.existsByClientAndNumber(client, number);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
