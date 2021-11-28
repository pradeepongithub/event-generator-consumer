DROP KEYSPACE IF EXISTS event_generator_service_blue;

CREATE KEYSPACE event_generator_service_blue WITH replication = {'class': 'SimpleStrategy', 'replication_factor':1};