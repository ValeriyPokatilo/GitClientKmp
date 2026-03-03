#!/bin/bash
set -euo pipefail

GITLAB_TOKEN=$1
GITLAB_PROJECT_ID=$2
FASTLANE_PASSWORD=$3
MATCH_PASSWORD=$4

FASTLANE_USER="apple@icerockdev.com"
FASTLANE_TEAM_ID="4LK42L3F84"
MATCH_GIT_BRANCH="aisrok_kazakhstan"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_PASSWORD" --form "value=$FASTLANE_PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_USER" --form "value=$FASTLANE_USER"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_PASSWORD" --form "value=$MATCH_PASSWORD"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=FASTLANE_TEAM_ID" --form "value=$FASTLANE_TEAM_ID"

curl --request POST --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
  "https://gitlab.icerockdev.com/api/v4/projects/$GITLAB_PROJECT_ID/variables" \
  --form "key=MATCH_GIT_BRANCH" --form "value=$MATCH_GIT_BRANCH"
