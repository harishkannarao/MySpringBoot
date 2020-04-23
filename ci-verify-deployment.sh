#!/usr/bin/env bash

VERSION_HOST=$URL
EXPECTED_SHA=$(echo $GITHUB_SHA | cut -c1-7)

ATTEMPT=1
MAX_ATTEMPTS=600
SLEEP_SECS=1
SUCCESS=-1

while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
   VERSION="$(curl -s $VERSION_HOST | jq -r '.commit')"
   SUCCESS=$?

   if [ $SUCCESS == 0 ] && [ $VERSION == "\"${EXPECTED_SHA}\"" ];
   then
      echo "Deployment appears to be up! Version: ${VERSION} returned from ${VERSION_HOST}"
      exit 0
   fi

   echo "[${ATTEMPT}/${MAX_ATTEMPTS}] Unable to locate version '${EXPECTED_SHA}' at ${VERSION_HOST} - will retry..."
   sleep ${SLEEP_SECS}
   ATTEMPT=$((ATTEMPT+1))
done

# Unable to verify deployment
exit -1