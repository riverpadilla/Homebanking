package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.enums.AccountType;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.services.AccountService;
import com.mindhubap.homebanking.services.ClientService;
import com.mindhubap.homebanking.services.TransactionService;
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
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts/all")
    public List<AccountDTO> getAccounts(){
        List<Account> accounts = accountService.findAllAccounts();
        return accountService.convertToAccountsDTO(accounts);
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
            Account account = accountService.findById(id);
            return new AccountDTO(account);
    }

    @PostMapping("/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam Long id)
    {
        Account account = accountService.findById(id);

        if (account == null){
            return new ResponseEntity<>("Account not exist", HttpStatus.BAD_REQUEST);
        }
        if (account.getBalance() > 0){
            return new ResponseEntity<>("The balance for the account to be deleted is greater than zero", HttpStatus.BAD_REQUEST);
        }
        if (!account.getTransactions().isEmpty()){
            for(Transaction transaction:account.getTransactions()){
                transactionService.deleteTransaction(transaction);
            }
        }
        accountService.deleteAccount(account);
        return new ResponseEntity<>("Account has been Deleted", HttpStatus.OK);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getCurrentAccounts(Authentication authentication)
    {
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type)
    {
        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() < 3) {
            String number;
            boolean check;
            do{
                number = Utils.generateAccountNumber();
                check = accountService.existsByNumber(number);
            }while(check);

            Account account= new Account(number, LocalDate.now(), 0, type, client);

            accountService.saveAccount(account);
            return new ResponseEntity<>("Account added to client " + account.getClient().getEmail() , HttpStatus.CREATED);

        }

        return new ResponseEntity<>("The Maximum Number of Accounts has been reached", HttpStatus.FORBIDDEN);
    }

    @GetMapping("clients/current/accounts/{id}")
    public ResponseEntity<Object> getCurrentAccount(Authentication authentication, @PathVariable Long id){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null){
            return new ResponseEntity<>("Account does not exist",HttpStatus.BAD_REQUEST);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("Authenticated Client is not Owner of this Account",HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteCurrentAccount(
            Authentication authentication,
            @RequestParam Long id
    )
    {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);

        if (account == null){
            return new ResponseEntity<>("Account not exist", HttpStatus.BAD_REQUEST);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The Client " + client.getEmail()
                    + " is Not the Owner of account to be deleted", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() > 0){
            return new ResponseEntity<>("The balance for the account to be deleted is greater than zero", HttpStatus.BAD_REQUEST);
        }
        if (!account.getTransactions().isEmpty()){
            for(Transaction transaction:account.getTransactions()){
                transactionService.deleteTransaction(transaction);
            }
        }
        accountService.deleteAccount(account);
        return new ResponseEntity<>("Account has been Deleted", HttpStatus.OK);
    }
}
