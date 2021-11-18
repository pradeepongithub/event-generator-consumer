package com.event.generator.consumer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * This model class is for Event log data which contains event log key and payload information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "employee_event_log")
public class EventLog {

    @PrimaryKey
    private EventLogKey eventLogKey;

    @Column("event_payload")
    private String eventPayload;

    @Column("event_processed_time")
    private LocalDateTime eventProcessedTime;

    @Column("status")
    private String status;

}
