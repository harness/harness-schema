# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("//tools/bazel/pmd:defs.bzl", "pmd")
load("@rules_jvm_external//:specs.bzl", "maven")
#load("//:tools/bazel/GenTestRules.bzl", "run_tests_targets")


def resources(name = "resources", runtime_deps = [], testonly = 0, visibility = None):
    native.java_library(
        name = name,
        resources = native.glob(["**"], exclude = ["BUILD"]),
        resource_strip_prefix = "%s/" % native.package_name(),
        runtime_deps = runtime_deps,
        testonly = testonly,
        visibility = visibility,
    )

def getCheckstyleReportPathForSonar():
    return "../../../" + native.package_name() + "/checkstyle.xml"

def sonarqube_test(
        name = None,
        project_key = None,
        project_name = None,
        srcs = ["src/main/java/**/*.java"],
        source_encoding = None,
        targets = [],
        test_srcs = [],
        test_targets = [],
        test_reports = [],
        modules = {},
        sq_properties_template = None,
        tags = [],
        visibility = []):
    srcs = native.glob(srcs)
    targets = [":module"]
    if name == None:
        name = "sonarqube"
    if project_key == None:
        project_key = native.package_name().replace("/", ":")
    if project_name == None:
        project_name = "Portal :: " + native.package_name()
    if test_srcs == []:
        test_srcs = native.glob(["src/test/**/*.java"])
    if test_reports == []:
        test_reports = ["//:test_reports"]
    if tags == []:
        tags = ["manual", "no-ide", "sonarqube"]
    if visibility == []:
        visibility = ["//visibility:public"]

def run_analysis_per_module(
        checkstyle_srcs = ["*"],
        pmd_srcs = ["*"],
        sonarqube_srcs = ["*.java"],
        test_only = False,
        run_duplicated = True):
    run_analysis(checkstyle_srcs = checkstyle_srcs, pmd_srcs = pmd_srcs, sonarqube_srcs = sonarqube_srcs, run_pmd = not test_only, run_sonar = not test_only, run_duplicated = not test_only and run_duplicated)

def run_analysis(
        checkstyle_srcs = ["src/**/*"],
        pmd_srcs = ["src/main/**/*"],
        sonarqube_srcs = ["src/main/java/**/*.java"],
        run_checkstyle = True,
        run_pmd = True,
        run_sonar = True,
        run_duplicated = True,
        test_targets = []):

    if run_pmd:
        pmd(pmd_srcs)

    if run_sonar:
        sonarqube_test(srcs = sonarqube_srcs, test_targets = test_targets)

