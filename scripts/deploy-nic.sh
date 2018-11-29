#! /usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")"; pwd)"
. "$SCRIPT_DIR/utils.sh"

function usage {
  echo "Usage: $0  b"
  echo "Used to deploy your application to nestincloud"
}

if [[ -z "$1" ]]; then
  echo "Error: No app name provided"
  usage
  exit 1
fi

NIC_APP_NAME=$1

if [[ -z "$NIC_API_KEY" ]]; then
  echo "No NIC_API_KEY environment variable"
  usage
  exit 1
fi

frontBuild
serverCleanup
serverBuild

cd "$SERVER_DIR"
FILE_DIST="$(find target/universal -iname "*.zip")"

# Deploy
curl -u ":$NIC_API_KEY" "https://builder.nestincloud.io/assemblearchive?name=$NIC_APP_NAME" -F "dist=@$FILE_DIST" -F "ftype=zip"
