#!/usr/bin/env bash
# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

set -e

case ${BUILD_PURPOSE} in
    DEVELOPMENT|PR_CHECK|FEATURE|RELEASE|CACHE)  ;;
    *) exit 1 ;;
esac

PROPERTIES=build.properties
function getProperty () {
   cat "${PROPERTIES}" | grep "^$1=" | cut -d"=" -f2
}

echo "TIMESTAMP $(date +'%y%m%d-%H%M')"

if [ "${BUILD_PURPOSE}" = "RELEASE" ]
then
  echo STABLE_SIGNER_KEY_STORE=${KEY_STORE}
  echo STABLE_SIGNER_KEY_STORE_PASSWORD=${KEY_STORE_PASSWORD}
else
  echo STABLE_SIGNER_KEY_STORE `pwd`/project/signer/dummy_key_store.p12
  echo STABLE_SIGNER_KEY_STORE_PASSWORD dummy_store
fi

if [ "${BUILD_PURPOSE}" = "DEVELOPMENT" -o "${BUILD_PURPOSE}" = "PR_CHECK" -o "${BUILD_PURPOSE}" = "CACHE" ]
then
  echo "STABLE_GIT_BRANCH unknown-development-branch"
  echo "STABLE_GIT_COMMIT 0000000000000000000000000000000000000000"

  echo STABLE_MAJOR_VERSION 1
  echo STABLE_MINOR_VERSION 0
  echo STABLE_BUILD_NUMBER 0
  echo STABLE_PATCH dev
else
  echo "STABLE_GIT_BRANCH $(git rev-parse --abbrev-ref HEAD)"
  echo "STABLE_GIT_COMMIT $(git rev-parse HEAD)"

  echo STABLE_MAJOR_VERSION $(getProperty "build.majorVersion")
  echo STABLE_MINOR_VERSION $(getProperty "build.minorVersion")
  echo STABLE_BUILD_NUMBER $(getProperty "build.number")
  echo STABLE_PATCH $(getProperty "build.patch")
fi

echo "GIT_BRANCH_BASED_CONTAINER_TAG $(git rev-parse --abbrev-ref HEAD | tr / _)"
