# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("//:test-util.bzl", "DISTRIBUTE_TESTING_WORKER", "DISTRIBUTE_TESTING_WORKERS", "OPTIMIZED_PACKAGE_TESTS")

MAX_COMPILE_TESTS = 512
MAX_EXECUTION_TESTS = 20
SUITE_CODE_FILEPATH = "code_filepath"
SUITE_PACKAGE_NAME = "package_name"
SUITE_INDEX = "index"
SUITE_TEST_CLASS = "test_class"
COMBINED_TESTS_TARGET = "combined_tests"
TEST_PACKAGE_PATTERNS = ["src/test/java/", "src/test/"]  # needs to be ordered most specific to least specific

def run_tests_targets(srcs = "src/test/**/*Test.java"):
    targets = []
    test_files = native.glob([srcs])
    for idx in range(len(test_files)):
        test = normalize_path(test_files[idx])
        x = hash(test)
        if (x % DISTRIBUTE_TESTING_WORKERS != DISTRIBUTE_TESTING_WORKER):
            continue
        targets += [test]
    return targets

def normalize_path(path):
    for pattern in TEST_PACKAGE_PATTERNS:
        if pattern in path:  # For root level build file we have relative path from root
            return path[len(pattern):][:-5].replace("/", ".")

        # native.package_name() resolves to the package of the BUILD.bazel file that invokes the rule starting from the src root
        # e.g. 260-delegate/src/test/java/io/harness/delegate/app/platform, we are interested in Java package part.
        bazel_package = native.package_name()
        class_name = path[:-5].replace("/", ".")
        if pattern in bazel_package:  # For package level build files, we have just class names
            return bazel_package.split(pattern)[1].replace("/", ".") + "." + class_name

def run_tests(srcs = "src/test/**/*Test.java", **kwargs):
    targets = run_tests_targets(srcs)
    for test in targets:
        native.java_test(
            name = test,  # IntelliJ bazel plugin (mostly) assumes test target name is FQN of test class.
            runtime_deps = ["tests"],
            size = "enormous",
            jvm_flags = [
                "$(HARNESS_ARGS)",
                "-Xmx4G",
                "-XX:+HeapDumpOnOutOfMemoryError",
                "-XX:HeapDumpPath=heap.hprof",
            ],
            env = {"JAVA_HOME": "$(JAVABASE)"},
            toolchains = ["@bazel_tools//tools/jdk:current_host_java_runtime"],
            test_class = test,
            testonly = True,
            visibility = ["//visibility:private"],
            **kwargs
        )
    return targets

template = """
package %s;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ %s })
public class AllTests%s {
}

"""

def calculate_index(length, i):
    if length < MAX_EXECUTION_TESTS:
        return ""
    return str(i // MAX_EXECUTION_TESTS + 1)

def run_package_tests(deps = [], data = [], resources = []):
    if OPTIMIZED_PACKAGE_TESTS == 1:
        return optimized_run_package_tests(deps, data, resources)

    targets = []

    all_srcs = native.glob(["src/test/**/*.java"])

    directories = {}
    for src in all_srcs:
        directory = src[0:src.rfind("/")]
        srcs = directories.setdefault(directory, [])
        srcs.append(src)

    native.java_library(
        name = "shared_package_tests",
        testonly = True,
        srcs = native.glob(
            include = ["src/test/**/*.java"],
            exclude = ["src/test/**/*Test.java"],
        ),
        data = data,
        resources = resources,
        deps = deps,
    )

    for directory in directories.items():
        targets += junit_package_test(directory[0], directory[1], deps)

    return targets

def junit_package_test(directory, srcs, deps):
    truncate = len(directory) + 1

    shared_src = []
    all_tests = {}
    for src in srcs:
        if src.endswith("Test.java"):
            all_tests[src] = src[truncate:].replace(".java", ".class")
        else:
            shared_src += [src]

    targets = []

    if len(all_tests) == 0:
        return targets

    package = directory.replace("src/test/java/", "").replace("/", ".")

    for i in range(0, len(all_tests), MAX_EXECUTION_TESTS):
        tests = all_tests.items()[i:i + MAX_EXECUTION_TESTS]

        index = calculate_index(len(all_tests), i)

        code = template % (package, ", \n                      ".join([x[1] for x in tests]), index)

        test_class = "AllTests" + index

        code_filepath = [directory + "/" + test_class + ".java"]

        if (hash(code_filepath[0]) % DISTRIBUTE_TESTING_WORKERS != DISTRIBUTE_TESTING_WORKER):
            continue

        native.genrule(
            name = package + ".AllTests" + index + "-gen",
            outs = code_filepath,
            cmd = """
cat <<EOF >> $@
%s
EOF""" % code,
        )

        target_name = package + ".tests" + index
        tags = "exclusive"
        native.java_test(
            name = package + ".tests" + index,
            test_class = package + "." + test_class,
            deps = [":shared_package_tests"] + deps,
            size = "enormous",

            # inputs
            srcs = code_filepath + [x[0] for x in tests],

            #Additional
            visibility = ["//visibility:public"],
            jvm_flags = [
                "$(HARNESS_ARGS)",
                "-Xmx16G",
                "-XX:+HeapDumpOnOutOfMemoryError",
                "-XX:HeapDumpPath=heap.hprof",
            ],
            env = {"JAVA_HOME": "$(JAVABASE)"},
            toolchains = ["@bazel_tools//tools/jdk:current_host_java_runtime"],
        )

        targets += [target_name]

    return targets

def optimized_junit_package_test_suites(directory, srcs):
    truncate = len(directory) + 1

    all_tests = {}
    for src in srcs:
        if src.endswith("Test.java"):
            all_tests[src] = src[truncate:].replace(".java", ".class")

    if len(all_tests) == 0:
        return {}

    package = directory.replace("src/test/java/", "").replace("/", ".")

    suites = {}

    for i in range(0, len(all_tests), MAX_EXECUTION_TESTS):
        tests = all_tests.items()[i:i + MAX_EXECUTION_TESTS]

        index = calculate_index(len(all_tests), i)

        code = template % (package, ", \n                      ".join([x[1] for x in tests]), index)

        test_class = "AllTests" + index

        code_filepath = directory + "/" + test_class + ".java"

        suite = package + ".AllTests" + index + "-gen"
        suites[suite] = {
            SUITE_CODE_FILEPATH: code_filepath,
            SUITE_PACKAGE_NAME: package,
            SUITE_INDEX: index,
            SUITE_TEST_CLASS: test_class,
        }

        native.genrule(
            name = suite,
            outs = [code_filepath],
            cmd = """
cat <<EOF >> $@
%s
EOF""" % code,
        )

    return suites

def optimized_package_test(combined_tests_target_index, package, index, test_class):
    if (hash(package) % DISTRIBUTE_TESTING_WORKERS != DISTRIBUTE_TESTING_WORKER):
        return []

    target_name = package + ".tests" + index
    tags = "exclusive"
    native.java_test(
        name = target_name,
        test_class = package + "." + test_class,
        runtime_deps = [COMBINED_TESTS_TARGET + str(combined_tests_target_index)],
        size = "enormous",

        #Additional
        visibility = ["//visibility:public"],
        jvm_flags = [
            "$(HARNESS_ARGS)",
            "-Xmx4G",
            "-XX:+HeapDumpOnOutOfMemoryError",
            "-XX:HeapDumpPath=heap.hprof",
        ],
    )
    return [target_name]

def optimized_run_package_tests(deps = [], data = [], resources = []):
    all_srcs = native.glob(["src/test/**/*.java"])

    all_directories = {}
    for src in all_srcs:
        directory = src[0:src.rfind("/")]
        srcs = all_directories.setdefault(directory, [])
        srcs.append(src)

    if len(all_directories) == 0:
        return []

    directories = {}
    key = all_directories.keys()[0]
    directory = all_directories.pop(key)
    directories[key] = directory
    total = len(directory)

    index = 1
    for directory in all_directories.items():
        length = len(directory[1])
        if total + length > MAX_COMPILE_TESTS:
            optimized_run_directories_package_tests(directories, index, deps, data, resources)
            index += 1
            directories = {}
            total = 0
        directories[directory[0]] = directory[1]
        total += len(directory[1])

    return optimized_run_directories_package_tests(directories, index, deps, data, resources)

def optimized_run_directories_package_tests(directories, combined_tests_target_index, deps = [], data = [], resources = []):
    srcs = []
    for directory in directories.values():
        srcs.extend(directory)

    packages = {}
    for directory in directories.items():
        packages.update(optimized_junit_package_test_suites(directory[0], directory[1]))

    native.java_library(
        name = COMBINED_TESTS_TARGET + str(combined_tests_target_index),
        testonly = True,
        srcs = srcs + [x[SUITE_CODE_FILEPATH] for x in packages.values()],
        data = data,
        resources = resources,
        deps = deps + packages.keys(),
    )

    targets = []
    for package in packages.items():
        targets += optimized_package_test(combined_tests_target_index, package[1][SUITE_PACKAGE_NAME], package[1][SUITE_INDEX], package[1][SUITE_TEST_CLASS])

    return targets
