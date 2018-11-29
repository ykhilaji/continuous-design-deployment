#! /usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")"; pwd)"
TOP_DIR="$(cd $SCRIPT_DIR/..; pwd)"
CLIENT_DIR="$TOP_DIR/client"
SERVER_DIR="$TOP_DIR/server"

logging() {
  echo "#>>>" `date +"%T"` $1
}

##### FRONT Application ####
frontBuild() {
  logging "Building front application"
  cd "$CLIENT_DIR"
  npm install
  npm run build
}

frontTest() {
  logging "Testing front application"
  cd "$CLIENT_DIR"
  npm install
  npm run build
  npm run test
}

##### SERVER Application ####
serverBuild() {
  logging "Building server"
  cd "$SERVER_DIR"
  sbt dist
}

serverTest() {
  logging "Testing server"
  cd "$SERVER_DIR"
  serverCleanup
  serverTestFormat

  sbt scapegoat test
}

serverTestFormat() {
  logging "Testing server format"
  SBT_OPTS="$SBT_OPTS -Dsbt.log.noformat=true"

  if [ ! "x$BUILD_ARGS" = "x" ]; then
    SBT_OPTS="$SBT_OPTS $BUILD_ARGS"
  fi

  logging "Building The Server"
  logging "Cleaning up SBT and updating artifacts"

  sbt -v $SBT_OPTS clean update

  logging "Checking if the code was properly formated"

  sbt -v $SBT_OPTS scalafmt test:scalafmt sbt:scalafmt

  git diff --exit-code || (serverWriteDiff && exit 3)
}

serverWriteDiff() {
  TEST_REPORT_DIR="target/test-reports"
  FORMAT_TEST_FILE="$TEST_REPORT_DIR/format.xml"
  logging "The code was not properly formated. Generating a report and aborting the build"
  mkdir -p $TEST_REPORT_DIR

  cat > $FORMAT_TEST_FILE << EOF
<?xml version="1.0" encoding="UTF-8"?>
<testsuite name="scalafmt" tests="1" errors="0" failures="1" time="1.0">
  <testcase time="1.0">
    <failure type="BuildError">Scalafmt check failed, see differences above.
To fix, format your sources using sbt scalafmt test:scalafmt sbt:scalafmt before submitting a pull request.
Additionally, please squash your commits (eg, use git commit --amend) if you are going to update this pull request.
EOF

  git diff --exit-code >> $FORMAT_TEST_FILE
  cat >> $FORMAT_TEST_FILE << EOF
    </failure>
  </testcase>
</testsuite>
EOF
}

serverCleanup() {
  cd "$SERVER_DIR"
  sbt clean
}
