#!/bin/bash

#set -x
set -e

scripts_dir="$(cd "`dirname "$0"`"; pwd)"

# Load config
. "$scripts_dir/env.sh"

if [[ -z "$1" ]]; then
  echo "error: you must set a job name to import"
  echo ""
  echo "usage: $0 job_name"
  exit 1
fi

output_file="config.xml"

JOB_NAME=$1

echo "job to import: '$JOB_NAME'"

curl -X GET --fail --silent --show-error -u "$API_USER":"$API_TOKEN" "$JENKINS_URL/job/$JOB_NAME/config.xml" > $output_file

echo "Success: Job imported into file $output_file"
