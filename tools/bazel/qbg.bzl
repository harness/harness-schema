# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("@rules_java//java:defs.bzl", "java_library")

gbq_tool = "@com_github_query_builder_generator//src:qbg"

def qbg_java_files(name):
    qbg_files = native.glob(["**/*.qbg"])

    srcs = []
    for idx in range(len(qbg_files)):
        file = qbg_files[idx]
        qbg_file = qbg_files[idx][:-4] + ".java"

        target = "java_gbg_" + file
        native.genrule(
            name = target,
            outs = [qbg_file],
            srcs = [file],
            tools = [gbq_tool],
            tags = ["manual", "no-ide"],
            cmd = "$(location %s) generate --input $(location %s) --root \"$(RULEDIR)\" --class %s > /dev/null" % (gbq_tool, file, qbg_file),
        )

        srcs += [qbg_file]

    native.filegroup(
        name = name,
        srcs = srcs,
        visibility = ["//visibility:public"],
    )
