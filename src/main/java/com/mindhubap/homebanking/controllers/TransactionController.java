package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.dtos.TransactionDTO;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.services.AccountService;
import com.mindhubap.homebanking.services.ClientService;
import com.mindhubap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;


    @GetMapping("/transactions/all")
    public List<TransactionDTO> getAllTransactions(){
       List<Transaction> transactions = transactionService.findAllTransactions();
       return transactionService.convertToTransactionDTO(transactions);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long id){
        Transaction transaction = transactionService.findById(id);
        if (transaction == null){
            return new ResponseEntity<>("Transaction does not Exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new TransactionDTO(transaction),HttpStatus.OK);
    }

    @GetMapping("clients/current/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().isEmpty()){
            return new ResponseEntity<>("`Client " + client.getEmail() + " don't have accounts", HttpStatus.BAD_REQUEST);
        }
        Set<Transaction> transactions = new HashSet<>();
        for (Account account:client.getAccounts()){
            transactions.addAll(account.getTransactions());
        }

        return new ResponseEntity<>(transactionService.convertToTransactionDTO(new ArrayList<>(transactions)),HttpStatus.OK);
    }

    @Transactional
    @PostMapping("clients/current/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam double amount,
                                                    @RequestParam String description,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber
                                  )
    {
        if (amount <= 0)
            return new ResponseEntity<>("Transaction Amount is less or equal to Zero", HttpStatus.FORBIDDEN);

        if (description.isBlank())
            return new ResponseEntity<>("Transaction Description is blank", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.isBlank())
            return new ResponseEntity<>("Origin Account is blank", HttpStatus.FORBIDDEN);

        if (toAccountNumber.isBlank())
            return new ResponseEntity<>("Destination Account is blank", HttpStatus.FORBIDDEN);

        if (fromAccountNumber.equals(toAccountNumber))
            return new ResponseEntity<>("Origin and Destination Accounts are equals", HttpStatus.FORBIDDEN);

        Account originAccount = accountService.findByNumber(fromAccountNumber);
        Account destinationAccount = accountService.findByNumber(toAccountNumber);
        Client client = clientService.findByEmail(authentication.getName());

        if (originAccount == null)
            return new ResponseEntity<>("Origin Account Does Not Exist", HttpStatus.FORBIDDEN);

        if (!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("The Client " + client.getEmail()
                    + " is Not the Owner of Origin Account", HttpStatus.FORBIDDEN);
        }

        if (destinationAccount == null)
            return new ResponseEntity<>("destination Account Does Not Exist", HttpStatus.FORBIDDEN);

        if (originAccount.getBalance() < amount)
            return new ResponseEntity<>("Origin Account without enough balance", HttpStatus.FORBIDDEN);


        double originBalance = originAccount.getBalance() - amount;
        double destinationBalance = (destinationAccount.getBalance() + amount);
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT,-amount,description
                + " [" + originAccount.getNumber() + "]", LocalDateTime.now(), originBalance, true);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT,amount,description
                + " [" + destinationAccount.getNumber() + "]", LocalDateTime.now(), destinationBalance, true);
        originAccount.addTransaction(debitTransaction);
        destinationAccount.addTransaction(creditTransaction);
        originAccount.setBalance(originBalance);
        destinationAccount.setBalance(destinationBalance);
        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinationAccount);
        transactionService.saveTransaction(debitTransaction);
        transactionService.saveTransaction(creditTransaction);

        return new ResponseEntity<>("Transaction successful", HttpStatus.CREATED);
    }

}
