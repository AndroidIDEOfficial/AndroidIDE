#!/bin/bash

#
#  This file is part of AndroidIDE.
#
#  AndroidIDE is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  AndroidIDE is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
#

# Fail on error and print out commands
set -ex

# By default we don't shard
SHARD_COUNT=0
SHARD_INDEX=0
# By default we don't log
LOG_FILE=""
# By default we run tests on device
DEVICE=true

# The task for installing the tools in the emulator
INSTALLER_TEST_TASK_NAME=":app:connectedArm64-v8aDebugAndroidTest"
# Fully qualified name of the class which install installs the build tools
INSTALLER_TEST_CLASS_NAME="com.itsaky.androidide.BuildToolsInstallerTest"

# Common Gradle invocation arguments
GRADLE_INVOCATION_ARGS="--scan --continue --no-configuration-cache --stacktrace"

# Parse parameters
for i in "$@"; do
  case $i in
  --shard-count=*)
    SHARD_COUNT="${i#*=}"
    shift
    ;;
  --unit-tests)
    DEVICE=false
    shift
    ;;
  --shard-index=*)
    SHARD_INDEX="${i#*=}"
    shift
    ;;
  --log-file=*)
    LOG_FILE="${i#*=}"
    shift
    ;;
  --run-affected)
    RUN_AFFECTED=true
    shift
    ;;
  --run-flaky-tests)
    RUN_FLAKY=true
    shift
    ;;
  --affected-base-ref=*)
    BASE_REF="${i#*=}"
    shift
    ;;
  *)
    echo "Unknown option"
    exit 1
    ;;
  esac
done

# Start logcat if we have a file to log to
if [[ -n "$LOG_FILE" ]]; then
  adb logcat >"$LOG_FILE" &
fi

FILTER_OPTS=""
# Filter out flaky tests if we're not set to run them
if [[ -z "$RUN_FLAKY" ]]; then
  FILTER_OPTS="$FILTER_OPTS -Pandroid.testInstrumentationRunnerArguments.notAnnotation=androidx.test.filters.FlakyTest"
fi

# If we're set to only run affected test, update the Gradle task
if [[ -n "$RUN_AFFECTED" ]]; then
  if [ "$DEVICE" = true ]; then
    TASK="runAffectedAndroidTests"
  else
    TASK="runAffectedUnitTests"
  fi
  TASK="$TASK -Paffected_module_detector.enable"

  # If we have a base branch set, add the Gradle property
  if [[ -n "$BASE_REF" ]]; then
    TASK="$TASK -Paffected_base_ref=$BASE_REF"
  fi
fi

# If we don't have a task yet, use the defaults
if [[ -z "$TASK" ]]; then
  if [ "$DEVICE" = true ]; then
    TASK="connectedCheck"
  else
    TASK="testDebug"
  fi
fi

SHARD_OPTS=""
if [ "$SHARD_COUNT" -gt "0" ]; then
  # If we have a shard count value, create the necessary Gradle property args.
  # We assume that SHARD_INDEX has been set too
  SHARD_OPTS="$SHARD_OPTS -Pandroid.testInstrumentationRunnerArguments.numShards=$SHARD_COUNT"
  SHARD_OPTS="$SHARD_OPTS -Pandroid.testInstrumentationRunnerArguments.shardIndex=$SHARD_INDEX"
fi

# Run the build tools installer test
# shellcheck disable=SC2086
./gradlew \
  $GRADLE_INVOCATION_ARGS \
  $INSTALLER_TEST_TASK_NAME \
  -Pandroid.testInstrumentationRunnerArguments.class=$INSTALLER_TEST_CLASS_NAME

# Run the actual tests
# shellcheck disable=SC2086
./gradlew $GRADLE_INVOCATION_ARGS $TASK $FILTER_OPTS $SHARD_OPTS