package com.event.generator.consumer.consumer;

import com.event.generator.consumer.service.EventConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
@Slf4j
public class EventConsumer {

    private final EventConsumerService eventConsumerService;

    public EventConsumer(EventConsumerService eventConsumerService) {
        this.eventConsumerService = eventConsumerService;
    }

    @KafkaListener(topics = "${kafka.adf.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeEvents(@Payload String payload,
                              @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                              @Header("eventType") String eventTypeHeader,
                              @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long createdTimestamp) {

        log.info("Event Type {}, Key {}, createdTimestamp {} and payload {}", eventTypeHeader, key, LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneId.systemDefault()), payload);
        eventConsumerService.logEvent(eventTypeHeader, key, payload, LocalDateTime.ofInstant(Instant.ofEpochMilli(createdTimestamp), ZoneId.systemDefault()), "PROCESSED");

    }
}
