package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.AccountDTO;
import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.CardRepository;
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
@RequestMapping("api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/cards")
    private List<CardDTO> getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @RequestMapping("/cards/{id}")
    private CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @RequestMapping(path = "/clients/current/cards")
    private Set<CardDTO> getCurrentCards(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCard(Authentication authentication,)
    {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getCards().size() < 3) {

            Card card = new Card("", LocalDate.now(), 0, client);
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
