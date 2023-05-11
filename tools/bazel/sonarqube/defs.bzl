# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

def sonarqube_coverage_generator_binary():
    deps = ["@remote_coverage_tools//:all_lcov_merger_lib"]

    native.java_binary(
        name = "SonarQubeCoverageGenerator",
        srcs = [
            "src/main/java/com/google/devtools/coverageoutputgenerator/SonarQubeCoverageGenerator.java",
            "src/main/java/com/google/devtools/coverageoutputgenerator/SonarQubeCoverageReportPrinter.java",
        ],
        main_class = "com.google.devtools.coverageoutputgenerator.SonarQubeCoverageGenerator",
        deps = deps,
    )

def _build_sonar_project_properties(ctx, sq_properties_file):
    module_path = ctx.build_file_path.replace("BUILD", "")
    depth = len(module_path.split("/")) - 1
    parent_path = "../" * depth

    # SonarQube requires test reports to be named like TEST-foo.xml, so we step
    # through `test_targets` to find the matching `test_reports` values, and
    # symlink them to the usable name

    if hasattr(ctx.attr, "test_targets") and ctx.attr.test_targets and hasattr(ctx.attr, "test_reports") and ctx.attr.test_reports:
        test_reports_path = module_path + "test-reports"
        test_reports_runfiles = []

        for t in ctx.attr.test_targets:
            test_target = ctx.label.relative(t)
            bazel_test_report_path = "bazel-testlogs/" + test_target.package + "/" + test_target.name + "/test.xml"
            bazel_test_report = [t for t in ctx.files.test_reports if t.short_path == bazel_test_report_path]  #[0] or fail("Expected Bazel test report for %s with path %s" % (test_target, bazel_test_report_path))
            bazel_test_report.append(None)
            if bazel_test_report[0] == None:
                continue
            sq_test_report = ctx.actions.declare_file("%s/TEST-%s.xml" % (test_reports_path, test_target.name))
            ctx.actions.symlink(
                output = sq_test_report,
                target_file = bazel_test_report[0],
            )
            test_reports_runfiles.append(sq_test_report)
    else:
        test_reports_path = ""
        test_reports_runfiles = []

    if hasattr(ctx.attr, "coverage_report") and ctx.attr.coverage_report:
        coverage_report_path = parent_path + ctx.file.coverage_report.short_path
        coverage_runfiles = [ctx.file.coverage_report]
    else:
        coverage_report_path = ""
        coverage_runfiles = []

    java_files = _get_java_files([t for t in ctx.attr.targets if t[JavaInfo]])

    ctx.actions.expand_template(
        template = ctx.file.sq_properties_template,
        output = sq_properties_file,
        substitutions = {
            "{PROJECT_KEY}": ctx.attr.project_key,
            "{PROJECT_NAME}": ctx.attr.project_name,
            "{SOURCES}": ",".join([parent_path + f.short_path for f in ctx.files.srcs]),
            "{TEST_SOURCES}": ",".join([parent_path + f.short_path for f in ctx.files.test_srcs]),
            "{SOURCE_ENCODING}": ctx.attr.source_encoding,
            "{JAVA_BINARIES}": ",".join([parent_path + j.short_path for j in java_files["output_jars"].to_list()]),
            "{JAVA_LIBRARIES}": ",".join([parent_path + j.short_path for j in java_files["deps_jars"].to_list()]),
            "{MODULES}": ",".join(ctx.attr.modules.values()),
            "{TEST_REPORTS}": test_reports_path,
            "{COVERAGE_REPORT}": coverage_report_path,
            "{CHECKSTYLE_REPORT_PATH}": ctx.attr.checkstyle_report_path,
        },
        is_executable = False,
    )

    return ctx.runfiles(
        files = [sq_properties_file] + ctx.files.srcs + ctx.files.test_srcs + test_reports_runfiles + coverage_runfiles,
        transitive_files = depset(transitive = [java_files["output_jars"], java_files["deps_jars"]]),
    )

def _get_java_files(java_targets):
    return {
        "output_jars": depset(direct = [j.class_jar for t in java_targets for j in t[JavaInfo].outputs.jars]),
        "deps_jars": depset(transitive = [t[JavaInfo].transitive_deps for t in java_targets] + [t[JavaInfo].transitive_runtime_deps for t in java_targets]),
    }

def _test_report_path(parent_path, test_target):
    return parent_path + "bazel-testlogs/" + test_target.package + "/" + test_target.name

def _sonarqube_impl(ctx):
    sq_properties_file = ctx.actions.declare_file(ctx.attr.sq_properties_filename)

    local_runfiles = _build_sonar_project_properties(ctx, sq_properties_file)

    module_runfiles = ctx.runfiles(files = [])
    for module in ctx.attr.modules.keys():
        module_runfiles = module_runfiles.merge(module[DefaultInfo].default_runfiles)

    ctx.actions.write(
        output = ctx.outputs.executable,
        content = "\n".join([
            "#!/bin/bash",
            "echo 'Dereferencing bazel runfiles symlinks for accurate SCM resolution...'",
            "for f in $(find $(dirname %s) -type l); do sed -i '' $f; done" % sq_properties_file.short_path,
            "echo '... done.'",
            "exec %s -Dproject.settings=%s $@" % (ctx.executable.sonar_scanner.short_path, sq_properties_file.short_path),
        ]),
        is_executable = True,
    )

    return [DefaultInfo(
        runfiles = ctx.runfiles(files = [ctx.executable.sonar_scanner] + ctx.files.scm_info).merge(ctx.attr.sonar_scanner[DefaultInfo].default_runfiles).merge(local_runfiles).merge(module_runfiles),
    )]

_COMMON_ATTRS = dict(dict(), **{
    "project_key": attr.string(
        mandatory = True,
        doc = """SonarQube project key, e.g. `com.example.project:module`.""",
    ),
    "project_name": attr.string(
        doc = """SonarQube project display name.""",
    ),
    "srcs": attr.label_list(
        allow_files = True,
        default = [],
        doc = """Project sources to be analysed by SonarQube.""",
    ),
    "source_encoding": attr.string(
        default = "UTF-8",
        doc = """Source file encoding.""",
    ),
    "targets": attr.label_list(
        default = [],
        doc = """Bazel targets to be analysed by SonarQube.

            These may be used to provide additional provider information to the SQ analysis , e.g. Java classpath context.
            """,
    ),
    "modules": attr.label_keyed_string_dict(
        default = {},
        doc = """Sub-projects to associate with this SonarQube project.""",
    ),
    "test_srcs": attr.label_list(
        allow_files = True,
        default = [],
        doc = """Project test sources to be analysed by SonarQube. This must be set along with `test_reports` and `test_sources` for accurate test reporting.""",
    ),
    "test_targets": attr.string_list(
        default = [],
        doc = """A list of test targets relevant to the SQ project. This will be used with the `test_reports` attribute to generate the report paths in sonar-project.properties.""",
    ),
    "test_reports": attr.label_list(
        allow_files = True,
        default = [],
        doc = """Junit-format execution reports, e.g. `filegroup(name = "test_reports", srcs = glob(["bazel-testlogs/**/test.xml"]))`""",
    ),
    "sq_properties_template": attr.label(
        allow_single_file = True,
        default = "//tools/bazel/sonarqube:sonar-project.properties.tpl",
        doc = """Template file for sonar-project.properties.""",
    ),
    "sq_properties": attr.output(),
    "checkstyle_report_path": attr.string(
        default = "",
    ),
})

_sonarqube = rule(
    attrs = dict(_COMMON_ATTRS, **{
        "coverage_report": attr.label(
            allow_single_file = True,
            mandatory = False,
            doc = """Coverage file in SonarQube generic coverage report format.""",
        ),
        "scm_info": attr.label_list(
            allow_files = True,
            doc = """Source code metadata, e.g. `filegroup(name = "git_info", srcs = glob([".git/**"], exclude = [".git/**/*[*"],  # gitk creates temp files with []))`""",
        ),
        "sq_properties_filename": attr.string(
            doc = """Properties file for sonar project.""",
        ),
        "sonar_scanner": attr.label(
            executable = True,
            default = "//tools/bazel/sonarqube:sonar_scanner",
            cfg = "host",
            doc = """Bazel binary target to execute the SonarQube CLI Scanner""",
        ),
    }),
    fragments = ["jvm"],
    host_fragments = ["jvm"],
    implementation = _sonarqube_impl,
    executable = True,
)

def sonarqube(
        name,
        project_key,
        scm_info,
        coverage_report = None,
        project_name = None,
        srcs = [],
        source_encoding = None,
        targets = [],
        test_srcs = [],
        test_targets = [],
        test_reports = [],
        modules = {},
        sonar_scanner = None,
        sq_properties_template = None,
        sq_properties_filename = None,
        tags = [],
        visibility = [],
        checkstyle_report_path = ""):
    _sonarqube(
        name = name,
        project_key = project_key,
        project_name = project_name,
        scm_info = scm_info,
        srcs = srcs,
        source_encoding = source_encoding,
        targets = targets,
        modules = modules,
        test_srcs = test_srcs,
        test_targets = test_targets,
        test_reports = test_reports,
        coverage_report = coverage_report,
        sonar_scanner = sonar_scanner,
        sq_properties_template = sq_properties_template,
        sq_properties_filename = sq_properties_filename,
        tags = tags,
        visibility = visibility,
        checkstyle_report_path = checkstyle_report_path,
    )

def _sq_project_impl(ctx):
    local_runfiles = _build_sonar_project_properties(ctx, ctx.outputs.sq_properties)

    return [DefaultInfo(
        runfiles = local_runfiles,
    )]

_sq_project = rule(
    attrs = _COMMON_ATTRS,
    implementation = _sq_project_impl,
)

def sq_project(
        name,
        project_key,
        project_name = None,
        srcs = [],
        source_encoding = None,
        targets = [],
        test_srcs = [],
        test_targets = [],
        test_reports = [],
        modules = {},
        sq_properties_template = None,
        tags = [],
        visibility = [],
        checkstyle_report_path = ""):
    _sq_project(
        name = name,
        project_key = project_key,
        project_name = project_name,
        srcs = srcs,
        test_srcs = test_srcs,
        source_encoding = source_encoding,
        targets = targets,
        modules = modules,
        test_targets = test_targets,
        test_reports = test_reports,
        sq_properties_template = sq_properties_template,
        sq_properties = "sonar-project.properties",
        tags = tags,
        visibility = visibility,
        checkstyle_report_path = checkstyle_report_path,
    )
