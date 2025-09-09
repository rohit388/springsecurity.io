package com.springsecurity.io.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.io.dto.ExpenseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseProducer {

    private static final String TOPIC  = "expense-events";
    private static final Logger logger = LoggerFactory.getLogger(ExpenseProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ExpenseProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendExpenseEvent(ExpenseRequest expenseRequest){
        try{
            String key = String.valueOf(expenseRequest.getId());
            String value = objectMapper.writeValueAsString(expenseRequest);
            kafkaTemplate.send(TOPIC, key, value);
            logger.info("✅ Sent expense event: key={}, value={}", key, value);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing expense", e);
        }
    }
}

