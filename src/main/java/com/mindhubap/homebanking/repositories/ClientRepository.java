package com.mindhubap.homebanking.repositories;

import com.mindhubap.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
}
