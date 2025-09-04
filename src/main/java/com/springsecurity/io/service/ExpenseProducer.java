package com.springsecurity.io.service;

import com.springsecurity.io.dto.ExpenseRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExpenseProducer {

    private static final String TOPIC  = "expense-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ExpenseProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExpenseEvent(ExpenseRequest expenseRequest){
        try{

            String key = String.valueOf(expenseRequest.getId());
            String value = "{ \"id\": " + expenseRequest.getId() + ", \"title\": \"" + "I got it " + "\"}";
            kafkaTemplate.send(TOPIC, key, String.valueOf(expenseRequest));
            System.out.println("✅ Sent expense event: key=" + key + ", value=" + expenseRequest);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error serializing expense"+e);
        }
    }
}
