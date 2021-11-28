
CREATE TABLE event_generator_service_blue.employee_event_log (
id text,
event_creation_time timestamp,
event_name text,
event_payload text,
event_processed_time timestamp,
status text,
PRIMARY KEY (id, event_creation_time, event_name));