package org.example.cardservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.cardservice.entity.Card;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CardKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topic.account:default-account-topic}")
    private String topic;

    public void sendCardCreated(Card card) {
        String event = String.format(
                "{\"event\":\"CARD_CREATED\",\"accountId\":\"%s\",\"cardId\":\"%s\"}",
                card.getAccountId(),
                card.getId()
        );
        log.info("Producing CARD_CREATED event: {}", event);
        kafkaTemplate.send(topic, event);
    }

    public void sendCardDeleted(Card card) {
        String event = String.format(
                "{\"event\":\"CARD_DELETED\",\"accountId\":\"%s\",\"cardId\":\"%s\"}",
                card.getAccountId(),
                card.getId()
        );
        log.info("Producing CARD_DELETED event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}

