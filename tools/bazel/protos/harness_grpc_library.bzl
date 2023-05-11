# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("@rules_proto_grpc//java:defs.bzl", "java_grpc_compile", "java_proto_compile")

def harness_proto_library(**kwargs):
    # Compile protos
    name_pb = kwargs.get("name") + "_pb"
    java_proto_compile(
        name = name_pb,
        **{k: v for (k, v) in kwargs.items() if k in ("deps", "verbose")}  # Forward args
    )

    # Create java library
    native.java_library(
        name = kwargs.get("name"),
        srcs = [name_pb],
        deps = PROTO_DEPS,
        visibility = kwargs.get("visibility"),
    )

PROTO_DEPS = [
    "@maven//:com_google_guava_guava",
    "@maven//:com_google_protobuf_protobuf_java",
    "@maven//:javax_annotation_javax_annotation_api",
]

def harness_grpc_library(**kwargs):
    # Compile protos
    name_pb = kwargs.get("name") + "_pb"
    java_grpc_compile(
        name = name_pb,
        **{k: v for (k, v) in kwargs.items() if k in ("deps", "verbose")}  # Forward args
    )

    if kwargs.get("java_sources") != None:
        fail("do not use java_sources for the grpc library")

    native.java_library(
        name = kwargs.get("name"),
        srcs = [name_pb],
        deps = GRPC_DEPS,
        runtime_deps = ["@maven//:io_grpc_grpc_netty"],
        visibility = kwargs.get("visibility"),
    )

GRPC_DEPS = [
    "@maven//:com_google_guava_guava",
    "@maven//:com_google_protobuf_protobuf_java",
    "@maven//:com_google_protobuf_protobuf_java_util",
    "@maven//:javax_annotation_javax_annotation_api",
    "@maven//:io_grpc_grpc_core",
    "@maven//:io_grpc_grpc_protobuf",
    "@maven//:io_grpc_grpc_stub",
    "@maven//:io_grpc_grpc_alts",
    "@maven//:io_grpc_grpc_api",
    "@maven//:io_grpc_grpc_auth",
    "@maven//:io_grpc_grpc_context",
    "@maven//:io_grpc_grpc_grpclb",
    "@maven//:io_grpc_grpc_netty_shaded",
]
