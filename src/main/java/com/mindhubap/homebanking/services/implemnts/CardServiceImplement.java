package com.mindhubap.homebanking.services.implemnts;

import com.mindhubap.homebanking.dtos.CardDTO;
import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.models.Client;
import com.mindhubap.homebanking.repositories.CardRepository;
import com.mindhubap.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public Set<CardDTO> convertToCardDTO(Set<Card> cards) {
        return cards.stream().map(CardDTO :: new).collect(Collectors.toSet());
    }

    @Override
    public Long countByClientAndTypeAndActive(Client client, CardType cardType, boolean active) {
        return cardRepository.countByClientAndTypeAndActive(client, cardType, active);
    }

    @Override
    public boolean existsByClientAndTypeAndColorAndActive(Client client, CardType cardType, CardColor cardColor,boolean active) {
        return cardRepository.existsByClientAndTypeAndColorAndActive(client, cardType, cardColor, active);
    }

    @Override
    public boolean existsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Card card) {
        card.setActive(false);
        cardRepository.save(card);
    }

    @Override
    public Set<Card> findAllCardsByActive(boolean active) {
        return cardRepository.findAllByActive(active);
    }
}
