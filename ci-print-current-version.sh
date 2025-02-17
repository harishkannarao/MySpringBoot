#!/bin/sh

# Print the commands as it is executed. Useful for debugging
set -x

curl --retry 10 --retry-delay 1 --retry-all-errors -i -s $URL