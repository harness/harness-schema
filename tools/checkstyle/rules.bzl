# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

checkstyle_suppressions = "//tools/checkstyle:checkstyle-suppressions.xml"
checkstyle_xpath_suppressions = "//tools/checkstyle:checkstyle-xpath-suppressions.xml"
checkstyle_xml = "//tools/config/src/main/resources:harness_checks.xml"
checkstyle_jar = "//tools/checkstyle:checkstyle_deploy.jar"

def checkstyle(srcs = ["src/**/*"]):
    module_name = native.package_name()

    native.genrule(
        name = "checkstyle",
        srcs = native.glob(srcs),
        tags = ["manual", "no-ide", "analysis", "checkstyle"],
        outs = ["checkstyle.xml"],
        visibility = ["//visibility:public"],
        cmd = " ".join([
            "java -classpath $(location " + checkstyle_jar + ")",
            "-Dorg.checkstyle.google.suppressionfilter.config=$(location " + checkstyle_suppressions + ")",
            "-Dorg.checkstyle.google.suppressionxpathfilter.config=$(location " + checkstyle_xpath_suppressions + ")",
            "com.puppycrawl.tools.checkstyle.Main",
            "-c $(location " + checkstyle_xml + ")",
            "-f xml",
            "-x \"src/generated\"",
            "--",
            module_name,
            "> \"$@\"",
            "&& sed -Ei.bak 's|/private/(.*)/harness_monorepo/||g'  \"$@\"",
            "&& sed -Ei.bak 's|/tmp/(.*)/harness_monorepo/||g'  \"$@\"",
            "&& if grep -B 1 \"<error \" \"$@\"; then exit 1; fi",
        ]),
        tools = [
            checkstyle_xml,
            checkstyle_suppressions,
            checkstyle_xpath_suppressions,
            checkstyle_jar,
        ],
    )
