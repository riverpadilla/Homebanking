package com.mindhubap.homebanking;

import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository) {
		return (args) -> {
			repository.save(new Client("Melba", "Morel","melba@maindhub.com"));
			repository.save(new Client("Karym", "Padilla","karym@gmail.com"));
		};
	}
}
