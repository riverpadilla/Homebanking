package com.mindhubap.homebanking;

import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.enums.TransactionType;
import com.mindhubap.homebanking.models.*;
import com.mindhubap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			//create Clients
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
			Client client2 = new Client("Rivelino", "Padilla", "riverpadilla@msn.com", passwordEncoder.encode("654321"));
			//Create Admin
			Client admin = new Client("Admin", "SU", "admin@mindhub.com", passwordEncoder.encode("Admin"));


			//Save Clients in DB
			clientRepository.save(admin);
			clientRepository.save(client1);
			clientRepository.save(client2);

			// Create accounts
			Account account1 = new Account("VIN-12345678", LocalDate.now().minusDays(2), 5000);
			Account account2 = new Account("VIN-87654321", LocalDate.now().minusDays(1), 7500);



			// Add Account to Client
			client1.addAccount(account1);
			client2.addAccount(account2);


			// Save account in DB
			accountRepository.save(account1);
			accountRepository.save(account2);



			//Create Transactions
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1500, "Direct Deposit - Salary", LocalDateTime.now().minusDays(2));
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 1600, "Direct Deposit - Interest", LocalDateTime.now().minusDays(1));
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1700, "Rent", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 1800, "Payment", LocalDateTime.now().minusDays(2));

			// Add Transaction to Account
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			//Save Transaction on DB
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);


			// Create Loads
			Loan loan1 = new Loan("Mortgage", 500000, List.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000, List.of(6, 12, 24));
			Loan loan3 = new Loan("Automotive", 300000, List.of(6, 12, 24, 36));

			// Save Loans to DB
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			//Create ClientLoan
			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			// Add Client to ClientLoan
			client1.addLoan(clientLoan1);
			client1.addLoan(clientLoan2);
			client2.addLoan(clientLoan3);
			client2.addLoan(clientLoan4);

			//Add Loan to ClientLoan
			loan1.addClient(clientLoan1);
			loan2.addClient(clientLoan2);
			loan2.addClient(clientLoan3);
			loan3.addClient(clientLoan4);

			// Save ClientLoan to DB
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			// Create Cards
			String name1 = client1.getFirstName() + " " + client1.getLastName();
			Card card1 = new Card(name1, CardType.DEBIT, CardColor.GOLD, "4567-1234-8765-9876", (short)123, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card2 = new Card(name1, CardType.CREDIT, CardColor.TITANIUM, "4567-8765-1234-6789", (short)567, LocalDate.now(), LocalDate.now().plusYears(5));
			String name2 = client2.getFirstName() + " " + client2.getLastName();
			Card card3 = new Card(name2, CardType.CREDIT, CardColor.SILVER, "4567-7890-8765-1234", (short)246, LocalDate.now().minusYears(1), LocalDate.now().plusYears(4));

			// Add Card to Client
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			// Save Card to DB
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};
	}
}
