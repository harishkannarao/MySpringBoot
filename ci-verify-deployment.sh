#!/usr/bin/env bash

VERSION_HOST=$URL
EXPECTED_SHA=$(echo $GITHUB_SHA | cut -c1-7)

ATTEMPT=1
MAX_ATTEMPTS=600
SUCCESS_COUNT=1
MIN_SUCCESS_COUNT=10
SLEEP_SECS=1
SUCCESS=-1

while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
   VERSION="$(curl -s $VERSION_HOST | jq ".commit")"
   SUCCESS=$?

   if [ $SUCCESS == 0 ] && [ $VERSION == "\"${EXPECTED_SHA}\"" ];
   then
      if [ $SUCCESS_COUNT -le $MIN_SUCCESS_COUNT ]
      then
          echo "[${SUCCESS_COUNT}/${MIN_SUCCESS_COUNT}] Deployment appears to be up! Version: ${VERSION} returned from ${VERSION_HOST}"
          SUCCESS_COUNT=$((SUCCESS_COUNT+1))
          sleep ${SLEEP_SECS}
      else
          exit 0
      fi
   else
      echo "[${ATTEMPT}/${MAX_ATTEMPTS}] Unable to locate version '${EXPECTED_SHA}' at ${VERSION_HOST} - will retry..."
      sleep ${SLEEP_SECS}
      ATTEMPT=$((ATTEMPT+1))
   fi
done

# Unable to verify deployment
exit -1