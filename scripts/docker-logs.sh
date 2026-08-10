#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "PostgreSQL logs:"
docker compose -f "$PROJECT_DIR/docker-compose.yaml" logs -f postgres
