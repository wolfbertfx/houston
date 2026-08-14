#!/bin/bash
set -e

chmod +x init-multiple-dbs.sh

echo "Starting Houston backend development environment..."
export POSTGRES_MULTIPLE_DATABASES="control,analytics,processing"
docker compose -f dev_env_compose.yml up -d
