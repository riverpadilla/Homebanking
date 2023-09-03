package com.mindhubap.homebanking.controllers;


import com.mindhubap.homebanking.dtos.ClientDTO;

import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.ClientRepository;
import com.mindhubap.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
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

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("The email is already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        String number;
        boolean check;
        do{
            number = Utils.generateAccountNumber();
            check = accountRepository.existsByNumber(number);
        }while(check);

        Account account = new Account(number, LocalDate.now(),0);

        accountRepository.save(account);
        client.addAccount(account);
        clientRepository.save(client);

        return new ResponseEntity<>("Client " + client.getEmail() + " was created",HttpStatus.CREATED);
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}


