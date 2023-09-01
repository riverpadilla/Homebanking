package com.mindhubap.homebanking.repositories;

import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByNumber(String number);
    boolean existsByClientAndTypeAndColor(Client client, CardType type, CardColor color);

    Long countByClientAndType(Client client,CardType type);


}
