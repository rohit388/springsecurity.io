package com.springsecurity.io.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SummaryConsumer {

    private static final Logger logger = LoggerFactory.getLogger(SummaryConsumer.class);

    @KafkaListener(topics = "${kafka.topic.expense-events}", groupId = "${kafka.group.summary-group}")
    public void updateSummary(String event){
        logger.info("📊 Updating Summary: {}", event);
    }
}
