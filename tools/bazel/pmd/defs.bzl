# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

rulesets = "//tools/config/src/main/resources:harness_pmd_ruleset.xml"
pmd_tool = "//tools/bazel/pmd:pmd"

def pmd(srcs = ["src/main/**/*"]):
    module_name = native.package_name()
    native.genrule(
        name = "pmd",
        outs = ["pmd_report.xml"],
        srcs = native.glob(srcs),
        tags = ["manual", "no-ide", "analysis", "pmd"],
        visibility = ["//visibility:public"],
        cmd = " ".join([
            "$(location " + pmd_tool + ")",
            "-d " + module_name,
            "-f xml",
            "-failOnViolation False",
            "-R $(location " + rulesets + ")",
            "-language java",
            "-cache pmd_report.cache",
            "> \"$@\"",
            "&& sed -Ei.bak 's|/private/(.*)/harness_monorepo/||g'  \"$@\"",
            "&& sed -Ei.bak 's|/tmp/(.*)/harness_monorepo/||g'  \"$@\"",
            "&& if grep -B 1 \"<violation \" \"$@\"; then exit 1; fi",
        ]),
        tools = [
            pmd_tool,
            rulesets,
        ],
    )
