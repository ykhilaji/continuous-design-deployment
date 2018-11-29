#!/usr/bin/env bash

# Script used by jenkins to test the PRs

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")"; pwd)"
. "$SCRIPT_DIR/utils.sh"

frontTest
serverTest

logging "Finished Testing"
