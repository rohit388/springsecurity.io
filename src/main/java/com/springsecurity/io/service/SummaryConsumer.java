package com.springsecurity.io.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SummaryConsumer {
    @KafkaListener(topics = "expense-events", groupId = "expense-group")

    public void updateSummary(String event){
        System.out.println("📊 Updating Summary: " + event);
    }
}
