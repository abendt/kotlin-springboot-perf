#!/bin/bash

trap "exit" SIGINT

#limits=("100" "1000" "10000" "50000" "100000" "200000" "300000")
#wrk_args="--latency -t12 -c400 -d30s --timeout 30s"

wrk_args="--latency -t12 -c400 -d10s --timeout 30s"
limits=("1000")

function run_loadtest() {
    for ep in "$@"; do
        wrk $wrk_args http://localhost:8080/$ep?limit=$limit
        sleep 30
    done
}

function run_loadtest_cpu() {
    for ep in "$@"; do
    for limit in "${limits[@]}" ; do
        wrk $wrk_args http://localhost:8080/$ep?limit=$limit
        sleep 30
    done
    done
}

run_loadtest "noop" "suspendNoop"
run_loadtest "blockingIO" "monoIO1" "monoIO2" "dispatcherIO"
run_loadtest_cpu "cpu0" "cpu1" "cpu2" "cpu3" "cpu4"
