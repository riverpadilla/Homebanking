package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.services.CardService;
import com.mindhubap.homebanking.services.ClientService;
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
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        List<Card> cards = cardService.findAllCards();
        return cardService.convertToCardDTO(cards);
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id){
        return new CardDTO(cardService.findById(id));
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCurrentCards(Authentication authentication)
    {
        Client client = clientService.findByEmail(authentication.getName());
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
        Client client = clientService.findByEmail(authentication.getName());

        Long creditCardsCount = cardService.countByClientAndType(client, CardType.CREDIT);
        Long debitCardsCount = cardService.countByClientAndType(client, CardType.DEBIT);

        if (cardType == CardType.CREDIT && creditCardsCount >= 3) {
        return new ResponseEntity<>("The maximum number of Credit Cards has been reached", HttpStatus.FORBIDDEN);
        }

        if (cardType == CardType.DEBIT && debitCardsCount >= 3) {
            return new ResponseEntity<>("The maximum number of Debit Cards has been reached", HttpStatus.FORBIDDEN);
        }

        if (cardService.existsByClientAndTypeAndColor(client,cardType,cardColor)){
            String message = "A card with this color status currently exist for ";
            if (cardType == CardType.DEBIT) message += "Debit Cards";
            if (cardType == CardType.CREDIT) message += "Credit Cards";
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        String cardHolder = client.getFirstName() + " " + client.getLastName();
        String number;
        boolean check;
        do{
            number = Utils.generateCardNumber();
            check = cardService.existsByNumber(number);
        } while(check);

        short cvv = (short)(100 + Math.random() * 899);
        Card card = new Card(cardHolder, cardType, cardColor, number, cvv, LocalDate.now(),LocalDate.now().plusYears(5));
        client.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(client);

        return new ResponseEntity<>("Card added to client " + card.getCardHolder(), HttpStatus.CREATED);
    }

}
