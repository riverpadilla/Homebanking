package com.mindhubap.homebanking.controllers;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.dtos.CardPaymentDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardException;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.services.AccountService;
import com.mindhubap.homebanking.services.CardService;
import com.mindhubap.homebanking.services.ClientService;
import com.mindhubap.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhubap.homebanking.utils.Utils.*;


@RestController
@RequestMapping("api/")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/cards/all")
    public ResponseEntity<Object> getCards()
    {
        Set<Card> cards = new HashSet<>(cardService.findAllCards());
        if (cards.isEmpty()){
            return new ResponseEntity<>("Database does not contains cards", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cardService.convertToCardDTO(cards), HttpStatus.OK);
    }

    @GetMapping("/cards/active")
    public ResponseEntity<Object> getActiveCards()
    {
        Set<Card> cards = cardService.findAllCardsByActive(true);
        if (cards.isEmpty()){
            return new ResponseEntity<>("Database does not contains Active Cards", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cardService.convertToCardDTO(cards), HttpStatus.OK);
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
    public ResponseEntity<Object> deleteCard(@RequestParam Long id)
    {
        Card card = cardService.findById(id);

        if (card == null){
            return new ResponseEntity<>("Card not exist", HttpStatus.BAD_REQUEST);
        }

        cardService.deleteCard(card);
        return new ResponseEntity<>("Applied a Soft Delete to this Card", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/cards/postnet")
    public ResponseEntity<Object> paymentWithCard(@RequestBody CardPaymentDTO cardPaymentDTO)
    {

        Account linkedAccount = null;

        Card card = cardService.findByNumber(cardPaymentDTO.getNumber());
        if (card == null){
            return new ResponseEntity<>("Card with number sent Does Not Exists", HttpStatus.FORBIDDEN);
        }
        if (card.getCvv() != cardPaymentDTO.getCvv()){
            return new ResponseEntity<>("Card Verification Value Incorrect", HttpStatus.FORBIDDEN);
        }
        if (!card.isActive()){
            return new ResponseEntity<>("Card Does Exists (Deleted)", HttpStatus.FORBIDDEN);
        }
        if (!(card.getThruDate().getMonth().getValue() == cardPaymentDTO.getThruDate().getMonth().getValue())){
            return new ResponseEntity<>("Card Expiration Date is Wrong", HttpStatus.FORBIDDEN);
        }
        if (!(card.getThruDate().getYear() == cardPaymentDTO.getThruDate().getYear())){
            return new ResponseEntity<>("Card Expiration Date is Wrong", HttpStatus.FORBIDDEN);
        }
        if (card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("Card Expired in " + card.getThruDate(), HttpStatus.FORBIDDEN);
        }
        if (cardPaymentDTO.getAmount() <= 0){
            return new ResponseEntity<>("Payment Amount is Less or Equal to Zero", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(card.getClient().getEmail());
        if (client == null){
            return new ResponseEntity<>("Card not linked to any Client", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().isEmpty()){
            return new ResponseEntity<>("Client Does Not Have Accounts linked to this card", HttpStatus.FORBIDDEN);
        }
        for (Account account: client.getAccounts()){
            if (account.getBalance() >= cardPaymentDTO.getAmount())
            {
                linkedAccount = account;
                break;
            }
        }
        if (linkedAccount == null){
            return new ResponseEntity<>("Client Does Not Have Accounts with enough balance for payment", HttpStatus.FORBIDDEN);
        }

        double balance = linkedAccount.getBalance() - cardPaymentDTO.getAmount();
        String message = "POSTNET Payment - CARD [****-" + cardPaymentDTO.getNumber().substring(cardPaymentDTO.getNumber().length()-4) +"] "
                +  cardPaymentDTO.getDescription() + " [" + linkedAccount.getNumber() + "]";
        Transaction transaction = new Transaction(TransactionType.DEBIT, -cardPaymentDTO.getAmount(), message, LocalDateTime.now(), balance, true);
        linkedAccount.addTransaction(transaction);
        linkedAccount.setBalance(balance);
        transaction.setAccount(linkedAccount);
        accountService.saveAccount(linkedAccount);
        transactionService.saveTransaction(transaction);
        return new ResponseEntity<>("Payment Successful", HttpStatus.OK);
    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<Object> getCurrentCards(Authentication authentication)
    {
        Client client = clientService.findByEmail(authentication.getName());
        if (client.getCards().isEmpty()){
            return new ResponseEntity<>("`Client " + client.getEmail() + " don't have cards", HttpStatus.BAD_REQUEST);
        }
        Set<Card> cards = client.getCards().stream().filter(Card::isActive).collect(Collectors.toSet());
        return new ResponseEntity<>(cardService.convertToCardDTO(cards), HttpStatus.OK);
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

        Long creditCardsCount = cardService.countByClientAndTypeAndActive(client, CardType.CREDIT,true);
        Long debitCardsCount = cardService.countByClientAndTypeAndActive(client, CardType.DEBIT,true);

        if (cardType == CardType.CREDIT && creditCardsCount >= 3) {
            message = cardExceptionMessage(CardException.MAX_QTY_CREDIT,card);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        if (cardType == CardType.DEBIT && debitCardsCount >= 3) {
            message = cardExceptionMessage(CardException.MAX_QTY_DEBIT,card);
            return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
        }

        if (cardService.existsByClientAndTypeAndColorAndActive(client,cardType,cardColor,true)){
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
        card = new Card(cardHolder, cardType, cardColor, number, cvv, LocalDate.now(),LocalDate.now().plusYears(5),true);
        client.addCard(card);
        cardService.saveCard(card);
        clientService.saveClient(client);

        message = cardExceptionMessage(CardException.CREATED,card);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCurrentCard(
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
