package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.CardRepository;
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
@RequestMapping("api/")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCurrentCards(Authentication authentication)
    {
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getCards()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType
            )
    {
        Client client = clientRepository.findByEmail(authentication.getName());

        Long creditCardsCount = cardRepository.countByClientAndType(client, CardType.CREDIT);
        Long debitCardsCount = cardRepository.countByClientAndType(client, CardType.DEBIT);

        if ((cardType == CardType.CREDIT && creditCardsCount < 3) || (cardType == CardType.DEBIT && debitCardsCount < 3)){
            String cardHolder = client.getFirstName() + " " + client.getLastName();
            String number;
            boolean check;
            do{
                number = Utils.generateCardNumber();
                check = cardRepository.existsByNumber(number);
            }while(check);

            short cvv = (short)(100 + Math.random() * 899);
            Card card = new Card(cardHolder, cardType, cardColor, number, cvv, LocalDate.now(),LocalDate.now().plusYears(5));
            client.addCard(card);
            cardRepository.save(card);
            clientRepository.save(client);
            return new ResponseEntity<>("Card added to client " + card.getCardHolder(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>("The maximum number of cards of the same type has been reached", HttpStatus.FORBIDDEN);
    }

}
