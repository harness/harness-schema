load("@bazel_skylib//:bzl_library.bzl", "bzl_library")

exports_files([
    "checkstyle-suppressions.xml",
    "checkstyle-xpath-suppressions.xml",
    "rules.bzl",
])

bzl_library(
    name = "checkstyle-rules",
    srcs = ["rules.bzl"],
    visibility = ["//visibility:public"],
)

java_binary(
    name = "checkstyle",
    srcs = glob(["src/main/java/io/harness/**/*.java"]),
    main_class = "com.puppycrawl.tools.checkstyle.Main",
    resources = ["//tools/checkstyle/src/main/resources:resource"],
    visibility = ["//visibility:public"],
    deps = [
        "@maven_checkstyle//:antlr_antlr",
        "@maven_checkstyle//:com_github_sevntu_checkstyle_sevntu_checks",
        "@maven_checkstyle//:com_google_guava_guava",
        "@maven_checkstyle//:com_puppycrawl_tools_checkstyle",
        "@maven_checkstyle//:commons_beanutils_commons_beanutils",
        "@maven_checkstyle//:commons_collections_commons_collections",
        "@maven_checkstyle//:info_picocli_picocli",
        "@maven_checkstyle//:org_antlr_antlr4_runtime",
        "@maven_checkstyle//:org_slf4j_jcl_over_slf4j",
        "@maven_checkstyle//:org_slf4j_slf4j_api",
        "@maven_checkstyle//:org_slf4j_slf4j_simple",
    ],
)
