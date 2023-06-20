# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("@rules_jvm_external//:specs.bzl", "maven")

def resources(name = "resources", runtime_deps = [], testonly = 0, visibility = None):
    native.java_library(
        name = name,
        resources = native.glob(["**"], exclude = ["BUILD"]),
        resource_strip_prefix = "%s/" % native.package_name(),
        runtime_deps = runtime_deps,
        testonly = testonly,
        visibility = visibility,
    )
