package com.springsecurity.io.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseConsumer.class);


    @KafkaListener(topics = "${kafka.topic.expense-events}",groupId = "${kafka.group.expense-group}")
    public void sendNotification(ConsumerRecord<String,String>record) {
        logger.info("📩 Consumed expense event:");
        logger.info("   Key   = {}", record.key());
        logger.info("   Value = {}", record.value());
        logger.info("   Topic = {}", record.topic());
        logger.info("   Partition = {}", record.partition());
        logger.info("   Offset    = {}", record.offset());

    }
}
