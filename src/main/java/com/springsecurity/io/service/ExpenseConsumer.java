package com.springsecurity.io.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExpenseConsumer {

    @KafkaListener(topics = "expense-events",groupId = "expense-group")
    public void sendNotification(ConsumerRecord<String,String>record) {
        System.out.println("📩 Consumed expense event:");
        System.out.println("   Key   = " + record.key());
        System.out.println("   Value = " + record.value());
        System.out.println("   Topic = " + record.topic());
        System.out.println("   Partition = " + record.partition());
        System.out.println("   Offset    = " + record.offset());
        // logic for sending email / push notification
    }
}
