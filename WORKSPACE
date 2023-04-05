# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

workspace(name = "harness_monorepo")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("//tools/bazel/pmd:dependencies.bzl", "rules_pmd_dependencies")

rules_pmd_dependencies()

http_archive(
    name = "com_github_bazelbuild_buildtools",
    sha256 = "932160d5694e688cb7a05ac38efba4b9a90470c75f39716d85fb1d2f95eec96d",
    strip_prefix = "buildtools-4.0.1",
    url = "http://jfrogdev.dev.harness.io:80/artifactory/bazel-buildtools-github/archive/refs/tags/4.0.1.zip",
)

# Workaround for https://github.com/bazelbuild/bazel-gazelle/issues/1285. Ideally,
# we can remove this if gazelle ships a fix since we didn't need it before.
http_archive(
    name = "bazel_skylib",
    sha256 = "f7be3474d42aae265405a592bb7da8e171919d74c16f082a5457840f06054728",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "com_google_protobuf",
    sha256 = "6aff9834fd7c540875e1836967c8d14c6897e3785a2efac629f69860fb7834ff",
    strip_prefix = "protobuf-3.15.0",
    urls = [
        "http://jfrogdev.dev.harness.io:80/artifactory/protobuf-github/archive/v3.15.0.tar.gz",
        #"https://mirror.bazel.build/github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
        #"https://github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
    ],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

# Download the Go rules
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "56d8c5a5c91e1af73eca71a6fab2ced959b67c86d12ba37feedb0a2dfea441a6",
    urls = [
        "http://jfrogdev.dev.harness.io:80/artifactory/rules-go-github/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
    ],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "448e37e0dbf61d6fa8f00aaa12d191745e14f07c31cabfa731f0c8e8a4f41b97",
    urls = [
        "http://jfrogdev.dev.harness.io:80/artifactory/bazel-gazelle-github/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://mirror.bazel.build/github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
    ],
)

# Load and call the dependencies
load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")


###########################################################################################
########################################   Java code ######################################

http_archive(
    name = "rules_spring",
    sha256 = "9385652bb92d365675d1ca7c963672a8091dc5940a9e307104d3c92e7a789c8e",
    urls = [
        "http://jfrogdev.dev.harness.io:80/artifactory/rules-spring-github/download/2.1.4/rules-spring-2.1.4.zip",
    ],
)

RULES_JVM_EXTERNAL_TAG = "4.1"

http_archive(
    name = "rules_jvm_external",
    sha256 = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "http://jfrogdev.dev.harness.io:80/artifactory/rules-jvm-external-github/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

plain_artifacts = [
    "com.fasterxml.jackson.core:jackson-databind:2.13.4.2",
        "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.4",
        "org.springframework.data:spring-data-commons:2.7.7",
         "com.google.api.grpc:proto-google-common-protos:2.9.6",
          "com.google.api:api-common:1.8.1",
          "com.google.code.findbugs:annotations:3.0.0",
          "org.projectlombok:lombok:1.18.18",
]

maven_install(
    name = "maven",
    artifacts = plain_artifacts,
    maven_install_json = "//project:main_maven_install.json",
    override_targets = {
        "org.apache.commons:commons-io": "@maven//:commons_io_commons_io",
        "com.jcraft:jsch": "@maven//:com_jcraft_harness_jsch_0_1_54_harness_patch",
        "org.mongodb:mongodb-driver": "@maven//:org_mongodb_mongodb_driver_core",
    },
    repositories = [
        "http://jfrogdev.dev.harness.io:80/artifactory/portal-maven",
        "https://harness.jfrog.io/artifactory/harness-internal",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

http_archive(
    name = "rules_pkg",
    sha256 = "038f1caa773a7e35b3663865ffb003169c6a71dc995e39bf4815792f385d837d",
    urls = [
        "http://jfrogdev.dev.harness.io:80/artifactory/rules-pkg-github/download/0.4.0/rules_pkg-0.4.0.tar.gz",
        #"https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.4.0/rules_pkg-0.4.0.tar.gz",
        #"https://github.com/bazelbuild/rules_pkg/releases/download/0.4.0/rules_pkg-0.4.0.tar.gz",
    ],
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")

rules_pkg_dependencies()

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

http_archive(
    name = "jre_x64_linux_8u242b08",
    build_file_content = """
load("@rules_pkg//:pkg.bzl", "pkg_tar")

pkg_tar(
    name = "jre_x64_linux_8u242b08",
    package_dir = "/opt/harness-delegate",
    srcs = glob(["jdk8u242-b08-jre/**"]),
    strip_prefix = '.',
    visibility = ["//visibility:public"],
)
""",
    sha256 = "5edfaefdbb0469d8b24d61c8aef80c076611053b1738029c0232b9a632fe2708",
    urls = ["https://app.harness.io/storage/wingsdelegates/jre/openjdk-8u242/jre_x64_linux_8u242b08.tar.gz"],
)

http_file(
    name = "alpn_boot_8.1.13.v20181017",
    sha256 = "05165e53fd9aeb774f95178c85740c3ee9ea72a9ca489497df837cc397a5da06",
    urls = ["https://app.harness.io/public/shared/tools/alpn/release/8.1.13.v20181017/alpn-boot-8.1.13.v20181017.jar"],
)


# Contrib rules jvm for build cleaner.
