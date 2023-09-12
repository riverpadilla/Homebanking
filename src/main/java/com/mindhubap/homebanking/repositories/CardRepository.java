package com.mindhubap.homebanking.repositories;

import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumber(String number);

    boolean existsByClientAndTypeAndColorAndActive(Client client, CardType type, CardColor color, boolean active);

    Card findByNumber(String number);

    Long countByClientAndTypeAndActive(Client client,CardType type, boolean active);

    Set<Card> findAllByActive(boolean active);
}
