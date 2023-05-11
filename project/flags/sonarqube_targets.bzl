# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

def get_sonarqube_targets():
    return {
        "//999-annotations:sonarqube": "999-annotations",
        "//979-recaster:sonarqube": "980-recaster",
        "//980-commons:sonarqube": "980-commons",
        "//970-watcher-beans:sonarqube": "970-watcher-beans",
        "//970-telemetry-beans:sonarqube": "970-telemetry-beans",
        "//970-rbac-core:sonarqube": "970-rbac-core",
        "//970-ng-commons:sonarqube": "970-ng-commons",
        "//970-grpc:sonarqube": "970-grpc",
        "//970-api-services-beans:sonarqube": "970-api-services-beans",
        "//965-api-key-filter:sonarqube": "965-api-key-filter",
        "//960-yaml-sdk:sonarqube": "960-yaml-sdk",
        "//960-watcher:sonarqube": "960-watcher",
        "//960-persistence:sonarqube": "960-persistence",
    }
