load("@rules_java//java:defs.bzl", "java_test")

java_library(
    name = "tests",
    testonly = True,
    srcs = ["ProtoFileTest.java"],
    deps = [
        "//tools/build-cleaner/src/main/java/io/harness/buildcleaner/proto:module",
        "@maven//:junit_junit",
        "@maven//:org_assertj_assertj_core",
    ],
)

java_test(
    name = "ProtoFileTest",
    size = "small",
    runtime_deps = [":tests"],
)
