package com.event.generator.consumer.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;

/**
 * This model class is for key data of Event log details table
 */
@Data
@Builder
@PrimaryKeyClass
public class EventLogKey {

    @PrimaryKeyColumn(value = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @PrimaryKeyColumn(value = "event_creation_time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private LocalDateTime eventCreationTime;

    @PrimaryKeyColumn(value = "event_name", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String eventName;

}
