package com.mindhubap.homebanking;

import com.mindhubap.homebanking.enums.AccountType;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.*;
import com.mindhubap.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //Test with Account Repository
    @Test
    public void checkAccounts(){

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
        for (Account account : accounts){
            assertThat(account.getBalance(),notNullValue());
            assertThat(account.getClient(),instanceOf(Client.class));
            assertThat(account.getNumber(),not(blankOrNullString()));
            assertThat(account.getNumber(),instanceOf(String.class));
            assertThat(account.getCreationDate(),instanceOf(LocalDate.class));
        }
    }

    @Test
    public void countAccounts(){

        Long accountsCounter = accountRepository.count();
        assertThat(accountsCounter,greaterThanOrEqualTo(1L));
    }

    @Test
    public void addAccount(){
        Client client = clientRepository.findByEmail("riverpadilla@msn.com");
        Account addedAccount = new Account("VIN-13572468",LocalDate.now(),100, AccountType.SAVING, client, true);
        accountRepository.save(addedAccount);
        Account retrievedAccount = accountRepository.findByNumber("VIN-13572468");
        assertThat(retrievedAccount, notNullValue());
    }

    //Test with Card Repository
    @Test
    public void checkCards(){

        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
        for (Card card: cards){
            assertThat(card.getCardHolder(),not(blankOrNullString()));
            assertThat(card.getColor(),instanceOf(CardColor.class));
            assertThat(card.getType(),instanceOf(CardType.class));
            assertThat(card.getClient(),instanceOf(Client.class));
            assertThat(card.getNumber(),instanceOf(String.class));
            assertThat(card.getCvv(),instanceOf(Short.class));
            assertThat(card.getFromDate(),instanceOf(LocalDate.class));
            assertThat(card.getThruDate(),instanceOf(LocalDate.class));
        }

    }

    @Test
    public void cardsCount(){

        Long cardsCounter = cardRepository.count();
        assertThat(cardsCounter,greaterThanOrEqualTo(1L));
    }

    @Test
    public void addCard(){
        Card addedCard = new Card("Juan Perez",CardType.CREDIT, CardColor.GOLD, "1234-5678-9876-5432",
                (short) 333,LocalDate.now(),LocalDate.now().plusYears(5), true);
        cardRepository.save(addedCard);
        Card retrievedCard = cardRepository.findByNumber("1234-5678-9876-5432");
        assertThat(retrievedCard, notNullValue());
    }

    //Test with Client Repository

    @Test
    public void checkClients(){

        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
        for (Client client: clients) {
            assertThat(client.getEmail(), not(blankOrNullString()));
            assertThat(client.getFirstName(), not(blankOrNullString()));
            assertThat(client.getLastName(), not(blankOrNullString()));
            assertThat(client.getPassword(), not(blankOrNullString()));
        }
    }

    @Test
    public void clientsCount(){

        Long clientsCounter = clientRepository.count();
        assertThat(clientsCounter,greaterThanOrEqualTo(1L));
    }

    @Test
    public void addClient(){
        Client addedClient = new Client("Juan","Perez", "juan.perez@email.com", "test123");
        clientRepository.save(addedClient);
        Client retrievedClient = clientRepository.findByEmail("juan.perez@email.com");
        assertThat(retrievedClient, notNullValue());
    }

    // Tests with Loan Repository
    @Test
    public void existsLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void countLoans(){

        Long loansCounter = loanRepository.count();
        assertThat(loansCounter,equalTo(3L));
    }

    @Test
    public void existsPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existsMortgageLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Mortgage"))));
    }

    @Test
    public void existsAutomotiveLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Automotive"))));
    }

    //Test with Transaction Repository

    @Test
    public void existsTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
        for (Transaction transaction : transactions){
            assertThat(transaction.getType(), instanceOf(TransactionType.class));
            assertThat(transaction.getDescription(),not(blankOrNullString()));
            assertThat(transaction.getDate(),instanceOf(LocalDateTime.class));
            assertThat(transaction.getAmount(),notNullValue());
        }
    }

    @Test
    public void transactionsCount(){

        Long transactionsCounter = transactionRepository.count();
        assertThat(transactionsCounter,greaterThanOrEqualTo(1L));
    }

}
