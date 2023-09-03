package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.dtos.LoanApplicationDTO;
import com.mindhubap.homebanking.dtos.LoanDTO;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.*;
import com.mindhubap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository  accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @PostMapping("/loans")
    public ResponseEntity<Object> createClientLoan(Authentication authentication,
                                             @RequestBody LoanApplicationDTO loanApplicationDTO)
    {

        Long loanTypeId = loanApplicationDTO.getLoanId();
        double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountToNumber = loanApplicationDTO.getAccountToNumber();

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(accountToNumber);
        Loan loan = loanRepository.findById(loanTypeId).orElse(null);


        if(loan == null){
            return new ResponseEntity<>("Loan does not Exist", HttpStatus.FORBIDDEN);
        }
        if(account == null){
            return new ResponseEntity<>("Destination Account to credit loan does Not Exist", HttpStatus.FORBIDDEN);
        }
        if(!accountRepository.existsByClientAndNumber(client,account.getNumber())){
            return new ResponseEntity<>("Authenticated Client is Not Owner of Destination Account",
                    HttpStatus.FORBIDDEN);
        }
        if(amount == 0){
            return new ResponseEntity<>("Loan value Equal to Zero", HttpStatus.FORBIDDEN);
        }
        if(payments == 0){
            return new ResponseEntity<>("Loan payments Equal to Zero", HttpStatus.FORBIDDEN);
        }
            if(accountToNumber.isBlank()){
            return new ResponseEntity<>("Destination Account to credit loan Is Missing", HttpStatus.FORBIDDEN);
        }
        if(amount > loan.getMaxAmount()){
            return new ResponseEntity<>("Amount requested for the loan exceeds the maximum amount approved",
                    HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("Amount of Payment is not available for this Loan",
                    HttpStatus.FORBIDDEN);
        }

        double accountBalance = account.getBalance() + loanApplicationDTO.getAmount();
        Transaction transaction = new Transaction(TransactionType.CREDIT, amount,loan.getName() + " Loan Approved", LocalDateTime.now());

        ClientLoan clientLoan = new ClientLoan(1.2 * loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        client.addLoan(clientLoan);
        loan.addClient(clientLoan);
        account.setBalance(accountBalance);
        account.addTransaction(transaction);

        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Loan Created and accredited in account "
                + loanApplicationDTO.getAccountToNumber(), HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getAvailableLoans()
    {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet());
    }
}
