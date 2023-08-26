package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.ClientRepository;
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

    @RequestMapping("/accounts")
    private List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
    private AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @RequestMapping(path = "/clients/current/accounts")
    private Set<AccountDTO> getCurrentAccounts(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() < 3) {

            Account account= new Account("", LocalDate.now(), 0, client);
            account.generateNumber(accountRepository.findAll());
            accountRepository.save(account);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private String generateAccountNumber()
    {
        String number;
        boolean check;
        do {
            check=true;
            number = "VIN-" + String.format("%06d", 11111111 + (int)(Math.random() * 99999999));

            for(AccountDTO account:this.getAccounts())
            {
                System.out.println(account.getNumber());
                if(account.getNumber().equals(number)){
                   check=false;
                }
            }
        } while(!check);

        return number;
    }
}
