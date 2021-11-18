package com.event.generator.consumer.service;

import com.event.generator.consumer.domain.EventLog;

import java.time.LocalDateTime;
import java.util.List;

public interface EventConsumerService {
    void logEvent(String eventType, String payload, String key, LocalDateTime createdTimeStamp, String status);

    List<EventLog> getEvents(Integer batchSize);
}