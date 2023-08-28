package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.ClientRepository;
import com.mindhubap.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
            Account account = accountRepository.findById(id).orElse(null);
            if(account != null) return new AccountDTO(account);
            return null;
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentAccounts(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() < 3) {
            String number = Utils.generateAccountNumber(accountRepository.findAll());
            Account account= new Account(number, LocalDate.now(), 0, client);

            accountRepository.save(account);
            return new ResponseEntity<>("Account added to client " + account.getClient().getEmail() , HttpStatus.CREATED);

        }

        return new ResponseEntity<>("The Maximum Number of Accounts has been reached", HttpStatus.FORBIDDEN);
    }

    @GetMapping("clients/current/accounts/{id}")
    public AccountDTO getCurrentAccount(Authentication authentication, @PathVariable Long id){

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        if ((account != null) && (client.getAccounts().contains(account))){
            return new AccountDTO(account);
        }
        return null;
    }

}
