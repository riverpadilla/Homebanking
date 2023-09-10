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
    public Set<CardDTO> convertToCardDTO(List<Card> cards) {
        return cards.stream().map(CardDTO :: new).collect(Collectors.toSet());
    }

    @Override
    public Long countByClientAndType(Client client, CardType cardType) {
        return cardRepository.countByClientAndType(client, cardType);
    }

    @Override
    public boolean existsByClientAndTypeAndColor(Client client, CardType cardType, CardColor cardColor) {
        return cardRepository.existsByClientAndTypeAndColor(client, cardType, cardColor);
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
        cardRepository.delete(card);
    }
}
