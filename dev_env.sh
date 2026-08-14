#!/bin/bash
set -e

chmod +x init-multiple-dbs.sh

echo "Starting Houston backend development environment..."
export POSTGRES_MULTIPLE_DATABASES="processing,control"
docker compose -f back_dev_compose.yml up -d
