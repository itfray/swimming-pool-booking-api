#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "Starting PostgreSQL container..."
docker compose -f "$PROJECT_DIR/docker-compose.yaml" up -d

echo "Waiting for PostgreSQL to be ready..."
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
  if docker exec pool-booking-postgres pg_isready -U plbk >/dev/null 2>&1; then
    echo "✓ PostgreSQL is ready!"
    exit 0
  fi
  attempt=$((attempt + 1))
  echo "  Attempt $attempt/$max_attempts..."
  sleep 1
done

echo "✗ PostgreSQL failed to start within $max_attempts seconds"
docker compose -f "$PROJECT_DIR/docker-compose.yaml" logs postgres
exit 1
