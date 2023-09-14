package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.LoanApplicationDTO;
import com.mindhubap.homebanking.dtos.LoanDTO;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.*;
import com.mindhubap.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<String> createClientLoan(Authentication authentication,
                                             @RequestBody LoanApplicationDTO loanApplicationDTO)
    {
        Long loanTypeId = loanApplicationDTO.getLoanId();
        double amount = loanApplicationDTO.getAmount();
        Integer payments = loanApplicationDTO.getPayments();
        String accountToNumber = loanApplicationDTO.getAccountToNumber();

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accountToNumber);
        Loan loan = loanService.findById(loanTypeId);


        if(loan == null){
            return new ResponseEntity<>("Loan does not Exist", HttpStatus.FORBIDDEN);
        }
        if(accountToNumber.isBlank()){
            return new ResponseEntity<>("Destination Account to credit loan Is Missing", HttpStatus.FORBIDDEN);
        }
        if(account == null){
            return new ResponseEntity<>("Destination Account to credit loan does Not Exist", HttpStatus.FORBIDDEN);
        }
        if(!accountService.existsByClientAndNumber(client,account.getNumber())){
            return new ResponseEntity<>("Authenticated Client is Not Owner of Destination Account",
                    HttpStatus.FORBIDDEN);
        }
        if(amount <= 0){
            return new ResponseEntity<>("Loan value Less or Equal to Zero", HttpStatus.FORBIDDEN);
        }
        if(payments <= 0){
            return new ResponseEntity<>("Loan payments Less or Equal to Zero", HttpStatus.FORBIDDEN);
        }
        if(amount > loan.getMaxAmount()){
            return new ResponseEntity<>("Amount requested for the loan exceeds the maximum amount approved",
                    HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("Amount of Payments is not available for this Loan",
                    HttpStatus.FORBIDDEN);
        }

        double accountBalance = account.getBalance() + loanApplicationDTO.getAmount();
        Transaction transaction = new Transaction(TransactionType.CREDIT, amount,loan.getName() + " Loan Approved", LocalDateTime.now());

        ClientLoan clientLoan = new ClientLoan(1.2 * loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        client.addLoan(clientLoan);
        loan.addClient(clientLoan);
        account.setBalance(accountBalance);
        account.addTransaction(transaction);

        transactionService.saveTransaction(transaction);
        accountService.saveAccount(account);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Loan Created and accredited in account "
                + loanApplicationDTO.getAccountToNumber(), HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public Set<LoanDTO> getAvailableLoans()
    {
        List<Loan> loans = loanService.findAll();
        return loanService.convertToLoanDTO(loans);
    }
}
