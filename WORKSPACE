# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

workspace(name = "harness_monorepo")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")


http_archive(
    name = "com_github_bazelbuild_buildtools",
    sha256 = "932160d5694e688cb7a05ac38efba4b9a90470c75f39716d85fb1d2f95eec96d",
    strip_prefix = "buildtools-4.0.1",
    url = "https://harness-artifactory.harness.io/artifactory/bazel-buildtools-github/archive/refs/tags/4.0.1.zip",
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
        "https://harness-artifactory.harness.io/artifactory/protobuf-github/archive/v3.15.0.tar.gz",
        #"https://mirror.bazel.build/github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
        #"https://github.com/protocolbuffers/protobuf/archive/v3.14.0.tar.gz",
    ],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

#openapi_repositories(
#    codegen_cli_provider = "harness-swagger-v3",
#    codegen_cli_sha256 = "8153719ed24ff1bdcf6d5bbe8713d26091ff85a3b253c2224f6e5c8490e9643d",
#    codegen_cli_version = "3.0.35-harness-SNAPSHOT",
#)

# Download the Go rules
http_archive(
    name = "io_bazel_rules_go",
    sha256 = "56d8c5a5c91e1af73eca71a6fab2ced959b67c86d12ba37feedb0a2dfea441a6",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/rules-go-github/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://mirror.bazel.build/github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
        #"https://github.com/bazelbuild/rules_go/releases/download/v0.37.0/rules_go-v0.37.0.zip",
    ],
)

http_archive(
    name = "bazel_gazelle",
    sha256 = "448e37e0dbf61d6fa8f00aaa12d191745e14f07c31cabfa731f0c8e8a4f41b97",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/bazel-gazelle-github/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://mirror.bazel.build/github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
        #"https://github.com/bazelbuild/bazel-gazelle/releases/download/v0.28.0/bazel-gazelle-v0.28.0.tar.gz",
    ],
)

# Load and call the dependencies
load("@io_bazel_rules_go//go:deps.bzl", "go_register_toolchains", "go_rules_dependencies")
load("@bazel_gazelle//:deps.bzl", "gazelle_dependencies", "go_repository")

http_archive(
    name = "rules_rust",
    sha256 = "e6d835ee673f388aa5b62dc23d82db8fc76497e93fa47d8a4afe97abaf09b10d",
    strip_prefix = "rules_rust-f37b9d6a552e9412285e627f30cb124e709f4f7a",
    urls = [
        # Master branch as of 2021-01-27
        "https://harness-artifactory.harness.io/artifactory/rules-rust-github/archive/f37b9d6a552e9412285e627f30cb124e709f4f7a.tar.gz",
    ],
)

load("@rules_rust//rust:repositories.bzl", "rust_repositories")

rust_repositories(
    edition = "2018",
    version = "1.49.0",
)

go_rules_dependencies()

go_register_toolchains(version = "1.19.8")

gazelle_dependencies()

http_archive(
    name = "rules_proto_grpc",
    sha256 = "5f0f2fc0199810c65a2de148a52ba0aff14d631d4e8202f41aff6a9d590a471b",
    strip_prefix = "rules_proto_grpc-1.0.2",
    urls = ["https://harness-artifactory.harness.io/artifactory/rules-proto-grpc-github/archive/1.0.2.tar.gz"],
)

load("@rules_proto_grpc//:repositories.bzl", "rules_proto_grpc_repos", "rules_proto_grpc_toolchains")

rules_proto_grpc_toolchains()

rules_proto_grpc_repos()

load("@rules_proto_grpc//java:repositories.bzl", rules_proto_grpc_java_repos = "java_repos")

rules_proto_grpc_java_repos()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

http_archive(
    name = "com_github_query_builder_generator",
    sha256 = "d72449d0ed7848260c6421be3677633946de46dc69af6588dbb4bc938e9714db",
    strip_prefix = "query-builder-generator-0.1.23",
    urls = ["https://github.com/wings-software/query-builder-generator/archive/refs/tags/v0.1.23.zip"],
)

go_repository(
    name = "org_golang_google_protobuf",
    importpath = "google.golang.org/protobuf",
    sum = "h1:4MY060fB1DLGMB/7MBTLnwQUY6+F09GEiz6SsrNqyzM=",
    version = "v1.23.0",
)

###########################################################################################
########################################   Java code ######################################

http_archive(
    name = "rules_spring",
    sha256 = "9385652bb92d365675d1ca7c963672a8091dc5940a9e307104d3c92e7a789c8e",
    urls = [
        "https://harness-artifactory.harness.io/artifactory/rules-spring-github/download/2.1.4/rules-spring-2.1.4.zip",
    ],
)

RULES_JVM_EXTERNAL_TAG = "4.1"

http_archive(
    name = "rules_jvm_external",
    sha256 = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://harness-artifactory.harness.io/artifactory/rules-jvm-external-github/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

plain_artifacts = [
        "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2",
        "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:2.14.2",
        "org.springframework:spring-web:5.3.27",
        "org.springframework.data:spring-data-commons:2.7.7",
        "io.grpc:grpc-protobuf:1.50.1",
        "com.google.code.findbugs:annotations:3.0.0",
        "org.projectlombok:lombok:1.18.26",
]

powermock_artifacts = [
    maven.artifact(
        testonly = True,
        artifact = x,
        group = "org.powermock",
        version = "2.0.2",
    )
    for x in [
        "powermock-api-mockito2",
        "powermock-api-support",
        "powermock-core",
        "powermock-module-junit4",
        "powermock-module-junit4-common",
        "powermock-reflect",
    ]
]

maven_install(
    name = "maven",
    artifacts = plain_artifacts + powermock_artifacts + [
        maven.artifact(
            artifact = "netty-transport-native-kqueue",
            classifier = "osx-x86_64",
            group = "io.netty",
            version = "4.1.86.Final",
        ),
        maven.artifact(
            artifact = "jjschema",
            exclusions = [
                "javax.ws.rs:jsr311-api",
            ],
            group = "com.github.reinert",
            version = "1.0",
        ),
        maven.artifact(
            artifact = "google-api-services-container",
            exclusions = [
                "com.google.guava:guava-jdk5",
            ],
            group = "com.google.apis",
            version = "v1-rev48-1.23.0",
        ),
        maven.artifact(
            artifact = "clojure",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "1.9.0",
        ),
        maven.artifact(
            artifact = "core.specs.alpha",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "0.1.24",
        ),
        maven.artifact(
            artifact = "spec.alpha",
            exclusions = [
                "org.clojure:clojure",
            ],
            group = "org.clojure",
            version = "0.1.143",
        ),
        maven.artifact(
            artifact = "debezium-embedded",
            exclusions = [
                "log4j:log4j",
                "org.slf4j:slf4j-log4j12",
            ],
            group = "io.debezium",
            version = "1.7.2.Final",
        ),
        maven.artifact(
            artifact = "connect-runtime",
            exclusions = [
                "log4j:log4j",
                "org.slf4j:slf4j-log4j12",
            ],
            group = "org.apache.kafka",
            version = "2.8.1",
        ),
        maven.artifact(
            artifact = "opensaml-saml-impl",
            exclusions = [
                "org.apache.velocity:velocity-engine-core",
                "org.apache.velocity:velocity",
            ],
            group = "org.opensaml",
            version = "3.4.3",
        ),
        maven.artifact(
            artifact = "saml-client",
            exclusions = [
                "org.apache.velocity:velocity-engine-core",
                "org.apache.velocity:velocity",
            ],
            group = "com.coveo",
            version = "3.0.2",
        ),
    ],
    excluded_artifacts = [],
    maven_install_json = "//bundler/project:main_maven_install.json",
    override_targets = {
        "org.apache.commons:commons-io": "@maven//:commons_io_commons_io",
        "com.jcraft:jsch": "@maven//:com_jcraft_harness_jsch_0_1_54_harness_patch",
        "org.mongodb:mongodb-driver": "@maven//:org_mongodb_mongodb_driver_core",
    },
    repositories = [
        "https://harness-artifactory.harness.io/artifactory/portal-maven",
        "https://harness.jfrog.io/artifactory/harness-internal",
        "https://harness.jfrog.io/harness/thirdparty-annonymous",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()




http_archive(
    name = "openjdk8u242_linux_archive",
    build_file_content = """
java_runtime(name = 'runtime', srcs =  glob(['**']), java='//:bin/java', visibility = ['//visibility:public'])
exports_files(["WORKSPACE"], visibility = ["//visibility:public"])
""",
    sha256 = "f39b523c724d0e0047d238eb2bb17a9565a60574cf651206c867ee5fc000ab43",
    strip_prefix = "jdk8u242-b08",
    urls = ["https://harness-artifactory.harness.io/artifactory/adoptjdk8u242-b08-github/download/jdk8u242-b08/OpenJDK8U-jdk_x64_linux_hotspot_8u242b08.tar.gz"],
)

http_archive(
    name = "openjdk8u242_macos_archive",
    build_file_content = """
package(default_visibility = ["//visibility:public"])
java_runtime(
    name = 'runtime',
    srcs =  glob(['**']),
    java='//:Contents/Home/bin/java'
)
""",
    sha256 = "06675b7d65bce0313ee1f2e888dd44267e8afeced75e0b39b5ad1f5fdff54e0b",
    strip_prefix = "jdk8u242-b08",
    urls = ["https://harness-artifactory.harness.io/artifactory/adoptjdk8u242-b08-github/download/jdk8u242-b08/OpenJDK8U-jdk_x64_mac_hotspot_8u242b08.tar.gz"],
)



http_archive(
    name = "io_bazel_rules_docker",
    sha256 = "59d5b42ac315e7eadffa944e86e90c2990110a1c8075f1cd145f487e999d22b3",
    strip_prefix = "rules_docker-0.17.0",
    urls = ["https://harness-artifactory.harness.io/artifactory/rules-docker-github/download/v0.17.0/rules_docker-v0.17.0.tar.gz"],
)

load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)

container_repositories()

load(
    "@io_bazel_rules_docker//container:container.bzl",
    "container_pull",
)




