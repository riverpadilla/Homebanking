package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardException;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.services.CardService;
import com.mindhubap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mindhubap.homebanking.utils.Utils.*;


@RestController
@RequestMapping("api/")
public class CardController {
    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/cards/all")
    public ResponseEntity<Object> getCards()
    {
        List<Card> cards = cardService.findAllCards();
        if (cards.isEmpty()){
            return new ResponseEntity<>("Database does not contains cards", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cardService.convertToCardDTO(new ArrayList<>(cards)), HttpStatus.OK);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<Object> getCard(@PathVariable Long id)
    {
        Card card =cardService.findById(id);
        if (card == null){
            return new ResponseEntity<>("Card does not Exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CardDTO(card), HttpStatus.OK);
    }

    @PostMapping("/cards/delete")
    public ResponseEntity<Object> deleteCard(
            @RequestParam Long id
    )
    {
        Card card = cardService.findById(id);

        if (card == null){
            return new ResponseEntity<>("Card not exist", HttpStatus.BAD_REQUEST);
        }

        cardService.deleteCard(card);
        return new ResponseEntity<>("Card has been Deleted", HttpStatus.OK);
    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentCards(Authentication authentication)
    {
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getCards().isEmpty()){
            return new ResponseEntity<>("`Client " + client.getEmail() + " don't have cards", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(cardService.convertToCardDTO(new ArrayList<>(client.getCards())), HttpStatus.OK);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(
            Authentication authentication,
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType
            )
    {
        String message;

        Client client = clientService.findByEmail(authentication.getName());
        String cardHolder = client.getFirstName() + " " + client.getLastName();
        Card card = new Card(cardHolder, cardType,cardColor);

        Long creditCardsCount = cardService.countByClientAndType(client, CardType.CREDIT);
        Long debitCardsCount = cardService.countByClientAndType(client, CardType.DEBIT);

        if (cardType == CardType.CREDIT && creditCardsCount >= 3) {
            message = cardExceptionMessage(CardException.MAX_QTY_CREDIT,card);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        if (cardType == CardType.DEBIT && debitCardsCount >= 3) {
            message = cardExceptionMessage(CardException.MAX_QTY_DEBIT,card);
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        if (cardService.existsByClientAndTypeAndColor(client,cardType,cardColor)){
            message = cardExceptionMessage(CardException.EXIST,card);
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        String number;
        boolean check;
        do{
            number = generateCardNumber();
            check = cardService.existsByNumber(number);
        } while(check);

        short cvv = generateCvv();
        card = new Card(cardHolder, cardType, cardColor, number, cvv, LocalDate.now(),LocalDate.now().plusYears(5));
        client.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(client);

        message = cardExceptionMessage(CardException.CREATED,card);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCard(
            Authentication authentication,
            @RequestParam Long id
            )
    {
        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findById(id);

        if (card == null){
            return new ResponseEntity<>("Card not exist", HttpStatus.BAD_REQUEST);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("The Client " + client.getEmail()
                    + " is Not the Owner of card to be deleted", HttpStatus.FORBIDDEN);
        }

        cardService.deleteCard(card);
        return new ResponseEntity<>("Card has been Deleted", HttpStatus.OK);
    }
}
