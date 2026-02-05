package org.example.loansservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.example.loansservice.entity.Loan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoanKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoanKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${spring.kafka.topic.account:default-account-topic}")
    private String topic;

    public void sendLoanCreated(Loan loan) {
        String event = String.format(
                "{\"event\":\"LOAN_CREATED\",\"accountId\":\"%s\",\"loanId\":\"%s\"}",
                loan.getAccountId(),
                loan.getId()
        );
        log.info("Producing LOAN_CREATED event: {}", event);
        kafkaTemplate.send(topic, event);
    }

    public void sendLoanDeleted(Loan loan) {
        String event = String.format(
                "{\"event\":\"LOAN_DELETED\",\"accountId\":\"%s\",\"loanId\":\"%s\"}",
                loan.getAccountId(),
                loan.getId()
        );
        log.info("Producing LOAN_DELETED event: {}", event);
        kafkaTemplate.send(topic, event);
    }
}

