#!/bin/bash

script_dir="$(dirname "$(realpath "$BASH_SOURCE")")"
cd $script_dir

# Function to handle termination
cleanup() {
    echo "Terminating both processes..."
    kill $bg_pid  # Terminate the background process (Process A)
    exit 0        # Exit the script
}

# Trap Ctrl-C (SIGINT) to run the cleanup function
trap cleanup SIGINT

# Start Process A in the background
../gradlew assemble --continuous --quiet &
bg_pid=$!     # Store the PID of the background process

# Start Process B in the foreground
docker compose up --build --watch

kill $bg_pid

# Wait for the foreground process to finish (this part is optional)
wait $bg_pid
