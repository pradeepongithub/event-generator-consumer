package com.event.generator.consumer.service;

import com.event.generator.consumer.domain.EventLog;
import com.event.generator.consumer.domain.EventLogKey;
import com.event.generator.consumer.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventConsumerServiceImpl implements EventConsumerService {

    private final EventLogRepository eventLogRepository;

    @Override
    public void logEvent(String eventType, String id, String payload, LocalDateTime eventCreationTime, String status) {
     log.info("save employee card_swipe event : " + id);
        eventLogRepository.insert(EventLog.builder()
                .eventLogKey(EventLogKey.builder()
                        .id(id)
                        .eventCreationTime(eventCreationTime)
                        .eventName(eventType)
                        .build())
                .eventProcessedTime(LocalDateTime.now())
                .eventPayload(payload)
                .status(status)
                .build());
    }

    @Override
    public List<EventLog> getEvents(Integer batchSize) {
        return eventLogRepository.findAll(CassandraPageRequest.ofSize(batchSize)).getContent();
    }
}
