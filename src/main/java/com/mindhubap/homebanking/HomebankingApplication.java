package com.mindhubap.homebanking;

import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.models.Transaction;
import com.mindhubap.homebanking.models.TransactionType;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.ClientRepository;
import com.mindhubap.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository) {
		return (args) -> {
			//create Clients
			Client client1 = new Client("Melba", "Morel","melba@maindhub.com");
			Client client2 = new Client("Rivelino","Padilla","riverpadilla@msn.com");

			//Save Clients in DB
			clientRepository.save(client1);
			clientRepository.save(client2);

			// Create accounts
			Account account1 = new Account("VIN001", LocalDate.now().minusDays(2),5000);
			Account account2 = new Account("VIN002", LocalDate.now().minusDays(1),7500);
			Account account3 = new Account("VIN003", LocalDate.now().minusDays(3),8000);


			// Add Account to Client
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			// Save account in DB
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);


			//Create Transactions
			Transaction transaction1 = new Transaction(TransactionType.CREDIT,1500,"Direct Deposit - Salary", LocalDateTime.now().minusDays(2));
			Transaction transaction2 = new Transaction(TransactionType.CREDIT,1600,"Direct Deposit - Interest", LocalDateTime.now().minusDays(1));
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,1700,"Rent", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,1800,"Payment", LocalDateTime.now().minusDays(2));
			Transaction transaction5 = new Transaction(TransactionType.CREDIT,1900,"Direct Deposit - Interest", LocalDateTime.now().minusDays(1));
			Transaction transaction6 = new Transaction(TransactionType.CREDIT,2000,"Direct Deposit - Salary", LocalDateTime.now());
			Transaction transaction7 = new Transaction(TransactionType.DEBIT,-500,"Buy on Amazon", LocalDateTime.now().minusDays(2));
			Transaction transaction8 = new Transaction(TransactionType.DEBIT,-800,"Rent Payment", LocalDateTime.now().minusDays(1));
			Transaction transaction9 = new Transaction(TransactionType.DEBIT,-200,"Buy on EBay", LocalDateTime.now());
			Transaction transaction10 = new Transaction(TransactionType.DEBIT,-100,"Internet Service", LocalDateTime.now().minusDays(1));
			Transaction transaction11 = new Transaction(TransactionType.DEBIT,-250,"Public Services", LocalDateTime.now());
			Transaction transaction12 = new Transaction(TransactionType.DEBIT,-50,"Buy on AliExpress", LocalDateTime.now());

			// Add Transaction to Account
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account1.addTransaction(transaction7);
			account1.addTransaction(transaction8);
			account1.addTransaction(transaction9);
			account2.addTransaction(transaction10);
			account2.addTransaction(transaction11);
			account3.addTransaction(transaction12);

			//Save Transaction on DB
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);

		};
	}
}
