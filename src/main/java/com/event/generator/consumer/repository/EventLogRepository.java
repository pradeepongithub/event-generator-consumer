package com.event.generator.consumer.repository;

import com.event.generator.consumer.domain.EventLog;
import com.event.generator.consumer.domain.EventLogKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface EventLogRepository extends CassandraRepository<EventLog, EventLogKey> {
}
