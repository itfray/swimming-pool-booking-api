#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "Stopping and removing PostgreSQL container..."
docker compose -f "$PROJECT_DIR/docker-compose.yaml" down

echo "✓ PostgreSQL container stopped and removed"
