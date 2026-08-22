#!/bin/bash
set -e
set -u

function create_database() {
    local database=$1
    echo "Creating database '$database'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE DATABASE "$database";
        GRANT ALL PRIVILEGES ON DATABASE "$database" TO "$POSTGRES_USER";
EOSQL
}

function enable_timescaledb() {
    local database=$1
    if psql --username "$POSTGRES_USER" -d "$database" -tAc "SELECT 1 FROM pg_available_extensions WHERE name = 'timescaledb';" | grep -q 1; then
        echo "Enabling timescaledb extension in '$database'"
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" -d "$database" \
            -c "CREATE EXTENSION IF NOT EXISTS timescaledb;"
    else
        echo "timescaledb extension not available in '$database', skipping"
    fi
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
    echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
    for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
        create_database $db
    done
    echo "Multiple databases created"
fi

if [ -n "${TIMESCALE_MULTIPLE_DATABASES:-}" ]; then
    echo "Timescale databases creation requested: $TIMESCALE_MULTIPLE_DATABASES"
    for db in $(echo $TIMESCALE_MULTIPLE_DATABASES | tr ',' ' '); do
        create_database $db
        enable_timescaledb $db
    done
    echo "Timescale databases created"
fi
