#!/usr/bin/env bash
# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

set -e

STABLE_PROPERTIES=$1
shift 1

function getProperty () {
   cat "${STABLE_PROPERTIES}" | grep "^STABLE_$1 " | cut -d" " -f2
}

SIGNER_KEY_STORE=$(getProperty "SIGNER_KEY_STORE")
SIGNER_KEY_STORE_PASSWORD=$(getProperty "SIGNER_KEY_STORE_PASSWORD")

set +e

output=`eval $@`
errorCode=$?

if [ $errorCode != 0 ]; then echo $output; fi
exit $errorCode
