#!/bin/bash

if [ -z "$1" ]; then
  echo "Error: The first parameter is required!"
  exit 1
fi

# Variables
base_url="http://localhost:3000"
endpoint="/api/annotations"
username="admin"
password="admin"
current_time=$(date +%s)000  # Current time in milliseconds
id="$1"  # Replace with the actual ID

json_body=$(jo -p time="$current_time" tags=$(jo -a "gatling") text="$id starting")
echo $json_body

# Curl command
curl -v -X POST "$base_url$endpoint" \
     -u "$username:$password" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d "$json_body" \
     -w "%{http_code}" -o /dev/null -s | grep -q 200 && echo "Request successful!" || echo "Request failed!"
