#!/bin/sh

# Make the script to abort if any command fails
set -e

# Print the commands as it is executed. Useful for debugging
set -x

curl -v -H "Accept: application/vnd.github.everest-preview+json" -H "Authorization: token $GITHUB_ACCESS_TOKEN" --request POST --data '{"event_type": "do-master-development-ci"}' 'https://api.github.com/repos/harishkannarao/gradle-qa-acceptance-tests/dispatches'