package org.example.cardservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.cardservice.entity.Card;
import org.example.cardservice.kafka.CardKafkaProducer;
import org.example.cardservice.repository.CardRepository;
import org.example.cardservice.rest.AccountServiceClient;
import org.example.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Autowired
    private CardKafkaProducer cardKafkaProducer;

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card not found"));
    }

    public List<Card> getCardsByAccountId(Long accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    public Card saveCard(Card card) {
        if (accountServiceClient.accountExists(card.getAccountId())) {
            Card saved = cardRepository.save(card);
            // Émission d'un évènement Kafka pour mettre à jour le nombre de cartes
            cardKafkaProducer.sendCardCreated(saved);
            return saved;
        } else {
            throw new IllegalArgumentException("Account does not exist");
        }
    }

    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        cardRepository.deleteById(id);
        // Émission d'un évènement Kafka pour décrémenter le nombre de cartes
        cardKafkaProducer.sendCardDeleted(card);
    }

    @Transactional
    public void deleteCardByAccountId(Long accountId) {
        log.info("Deleting card by account id {}", accountId);
        cardRepository.deleteByAccountId(accountId);
    }
}
