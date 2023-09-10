package com.mindhubap.homebanking.controllers;


import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.dtos.ClientDTO;

import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.services.AccountService;
import com.mindhubap.homebanking.services.ClientService;
import com.mindhubap.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients/all")
    public ResponseEntity<Object> getClients()
    {
        List<Client> clients = clientService.findAllClients();
        if (clients.isEmpty()){
            return new ResponseEntity<>("Database does not contains clients", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(clientService.convertToClientsDTO(clients), HttpStatus.OK);

    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Long id)
    {
        Client client = clientService.findById(id);
        if (client == null){
            return new ResponseEntity<>("Client does not Exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password
            )
    {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("First name is blank", HttpStatus.FORBIDDEN);
        }

        if (lastName.isBlank()) {
            return new ResponseEntity<>("Last name is blank", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()){
            return new ResponseEntity<>("Email is blank", HttpStatus.FORBIDDEN);
        }

        if (!Utils.isValidEmail(email)){
            return new ResponseEntity<>("Email does not valid", HttpStatus.FORBIDDEN);
        }

        if (password.isBlank()) {
            return new ResponseEntity<>("Password is blank", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("The email is already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        String number;
        boolean check;
        do{
            number = Utils.generateAccountNumber();
            check = accountService.existsByNumber(number);
        }while(check);

        Account account = new Account(number, LocalDate.now(),0);

        accountService.saveAccount(account);
        client.addAccount(account);
        clientService.saveClient(client);

        return new ResponseEntity<>("Client " + client.getEmail() + " was created",HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ResponseEntity<Object> getCurrentClient(Authentication authentication)
    {
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null){
            return new ResponseEntity<>("Client does not Exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);

    }
}


