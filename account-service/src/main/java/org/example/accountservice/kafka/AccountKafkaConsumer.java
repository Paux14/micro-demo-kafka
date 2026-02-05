package org.example.accountservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.entity.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountKafkaConsumer {

    @Autowired
    private AccountRepository accountRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "account-events", groupId = "account-group")
    public void consumeAccountEvents(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            String eventType = event.get("event").asText();

            switch (eventType) {
                case "CARD_CREATED" -> updateCardCount(event, 1);
                case "CARD_DELETED" -> updateCardCount(event, -1);
                case "LOAN_CREATED" -> updateLoanCount(event, 1);
                case "LOAN_DELETED" -> updateLoanCount(event, -1);
                default -> log.debug("Event ignoré par AccountKafkaConsumer : {}", eventType);
            }

        } catch (JsonProcessingException e) {
            log.error("Erreur de parsing de l'évènement Kafka dans AccountKafkaConsumer", e);
            throw new RuntimeException(e);
        }
    }

    private void updateCardCount(JsonNode event, int delta) {
        Long accountId = Long.valueOf(event.get("accountId").asText());
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.warn("Compte {} introuvable pour mise à jour du nombre de cartes", accountId);
            return;
        }
        Integer current = account.getTotalCards() == null ? 0 : account.getTotalCards();
        account.setTotalCards(Math.max(0, current + delta));
        accountRepository.save(account);
        log.info("Mise à jour totalCards pour le compte {} : {} -> {}", accountId, current, account.getTotalCards());
    }

    private void updateLoanCount(JsonNode event, int delta) {
        Long accountId = Long.valueOf(event.get("accountId").asText());
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.warn("Compte {} introuvable pour mise à jour du nombre de prêts", accountId);
            return;
        }
        Integer current = account.getTotalLoans() == null ? 0 : account.getTotalLoans();
        account.setTotalLoans(Math.max(0, current + delta));
        accountRepository.save(account);
        log.info("Mise à jour totalLoans pour le compte {} : {} -> {}", accountId, current, account.getTotalLoans());
    }
}

