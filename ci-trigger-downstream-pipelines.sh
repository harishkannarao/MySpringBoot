#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

curl -v -H "Accept: application/vnd.github.everest-preview+json" -H "Authorization: token $GITHUB_ACCESS_TOKEN" --request POST --data '{"event_type": "do-tag-ci", "client_payload": { "tag": "v2.0"}}' 'https://api.github.com/repos/harishkannarao/kotlin/dispatches'