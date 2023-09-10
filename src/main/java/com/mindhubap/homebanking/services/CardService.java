package com.mindhubap.homebanking.services;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;

import java.util.List;
import java.util.Set;

public interface CardService {

    List<Card> findAllCards();

    Card findById(Long id);

    Card findByNumber(String number);

    Set<CardDTO> convertToCardDTO(List<Card> cards);

    Long countByClientAndType(Client client, CardType cardType);

    boolean existsByClientAndTypeAndColor( Client client,CardType cardType, CardColor cardColor);

    boolean existsByNumber(String number);

    void saveCard(Card card);

    void deleteCard(Card card);
}
