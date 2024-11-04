#!/bin/bash

trap "exit" SIGINT

function grafana_annotate {
    # Variables
    base_url="http://localhost:3000"
    endpoint="/api/annotations"
    username="admin"
    password="admin"
    current_time=$(date +%s)000  # Current time in milliseconds
    id="$1"

    json_body=$(jo -p time="$current_time" tags=$(jo -a "gatling") text="$id starting")

    # Curl command
    result=$(curl -X POST "$base_url$endpoint" \
         -u "$username:$password" \
         -H "Content-Type: application/json" \
         -H "Accept: application/json" \
         -d "$json_body" \
         -w "%{http_code}" -o /dev/null -s | grep -q 200 && echo "Request successful!" || echo "Request failed!")
}

#for ep in "noop" "suspendNoop" "blockingIO" "completableFutureIO" "deferredIO1"
for ep in "deferredIO2" "deferredIO2" "deferredIO3"
#for ep in "blockingIO" "cpu1" "cpu2" "cpu3"
do
    grafana_annotate $ep
    wrk --latency -t12 -c400 -d30s --timeout 30s http://localhost:8080/$ep
    sleep 10
done
