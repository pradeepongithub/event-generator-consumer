package com.event.generator.consumer;

import com.event.generator.consumer.domain.EventLog;
import com.event.generator.consumer.service.EventConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventConsumerService eventConsumerService;

    @GetMapping(value = "/events/{batchSize}", produces = {"application/json"})
    public ResponseEntity<List<EventLog>> getEvents(@PathVariable("batchSize") Integer batchSize) {
        return ResponseEntity.ok(eventConsumerService.getEvents(batchSize));
    }

}
