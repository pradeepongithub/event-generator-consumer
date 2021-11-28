#! /bin/bash
set -e

echo "cql scripts will run after cassandra starts..."

cqlsh -f /cql/create_keyspaces.cql cassandra
cqlsh -k event_generator_service_blue -f /cql/create_event_tables.cql cassandra