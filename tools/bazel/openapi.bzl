# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")

_SUPPORTED_PROVIDERS = {
    "swagger": {
        "artifact": "io.swagger:swagger-codegen-cli",
        "name": "io_swagger_swagger_codegen_cli",
    },
    "openapi": {
        "artifact": "org.openapitools:openapi-generator-cli",
        "name": "org_openapitools_openapi_generator_cli",
    },
    "harness-swagger-v3": {
        "artifact": "io.swagger.codegen.v3:swagger-codegen-cli",
        "name": "io_swagger_codegen_v3_swagger_codegen_cli",
    },
}

def openapi_repositories(codegen_cli_version = "2.4.16", codegen_cli_sha256 = "154b5a37254a3021a8cb669a1c57af78b45bb97e89e0425e3f055b1c79f74a93", prefix = "io_bazel_rules_openapi", codegen_cli_provider = "swagger"):
    jvm_maven_import_external(
        name = prefix + "_" + _SUPPORTED_PROVIDERS[codegen_cli_provider]["name"],
        artifact = _SUPPORTED_PROVIDERS[codegen_cli_provider]["artifact"] + ":" + codegen_cli_version,
        artifact_sha256 = codegen_cli_sha256,
        server_urls = ["https://harness.jfrog.io/artifactory/harness-internal"],
        licenses = ["notice"],  # Apache 2.0 License
    )
    native.bind(
        name = prefix + "/dependency/openapi-cli",
        actual = "@" + prefix + "_" + _SUPPORTED_PROVIDERS[codegen_cli_provider]["name"] + "//jar",
    )

def _comma_separated_pairs(pairs):
    return ",".join([
        "{}={}".format(k, v)
        for k, v in pairs.items()
    ])

def _generator_provider(ctx):
    codegen_provider = "openapi"
    if "io_swagger_swagger_codegen_cli" in ctx.file.codegen_cli.path:
        codegen_provider = "swagger"
    if "io_swagger_codegen_v3_swagger_codegen_cli" in ctx.file.codegen_cli.path:
        codegen_provider = "harness-swagger-v3"
    return codegen_provider

def _is_harness_swagger_v3_codegen(ctx):
    return _generator_provider(ctx) == "harness-swagger-v3"

def _is_swagger_codegen(ctx):
    return _generator_provider(ctx) == "swagger"

def _is_openapi_codegen(ctx):
    return _generator_provider(ctx) == "openapi"

def _openapi_major_version(ctx):
    name = ctx.file.codegen_cli.path.split("/").pop(-1)  # Extract JAR's name
    version = name.split("-").pop(-1).replace(".jar", "").split(".").pop(0)  #
    return int(version)

def _new_generator_command(ctx, gen_dir, rjars):
    java_path = ctx.attr._jdk[java_common.JavaRuntimeInfo].java_executable_exec_path
    gen_cmd = str(java_path)
    gen_cmd += " -cp {cli_jar}:{jars}".format(
        cli_jar = ctx.file.codegen_cli.path,
        jars = ":".join([j.path for j in rjars.to_list()]),
    )

    if _is_harness_swagger_v3_codegen(ctx):
        gen_cmd += " -Dlogback.configurationFile={logback_test} io.swagger.codegen.v3.cli.SwaggerCodegen generate -i {spec} -c {config_file} -l {language} -o {output}".format(
            spec = ctx.file.spec.path,
            config_file = ctx.file.config_file.path,
            language = ctx.attr.language,
            output = gen_dir,
            logback_test = ctx.file.logback_test.path,
        )
        gen_cmd += ' -D "{properties}"'.format(
            properties = _comma_separated_pairs(ctx.attr.system_properties),
        )

    if _is_swagger_codegen(ctx):
        gen_cmd += " io.swagger.codegen.SwaggerCodegen generate -i {spec} -c {config_file} -l {language} -o {output}".format(
            spec = ctx.file.spec.path,
            config_file = ctx.file.config_file.path,
            language = ctx.attr.language,
            output = gen_dir,
        )
        gen_cmd += ' -D "{properties}"'.format(
            properties = _comma_separated_pairs(ctx.attr.system_properties),
        )

    if _is_openapi_codegen(ctx):
        gen_cmd += " org.openapitools.codegen.OpenAPIGenerator generate -i {spec} -c {config_file} -g {language} -o {output}".format(
            spec = ctx.file.spec.path,
            config_file = ctx.file.config_file.path,
            language = ctx.attr.language,
            output = gen_dir,
        )
        if _openapi_major_version(ctx) >= 5:
            gen_cmd += ' --global-property "{properties}"'.format(
                properties = _comma_separated_pairs(ctx.attr.system_properties),
            )
        else:
            gen_cmd += ' -D "{properties}"'.format(
                properties = _comma_separated_pairs(ctx.attr.system_properties),
            )

    additional_properties = dict(ctx.attr.additional_properties)

    if ctx.attr.language == "java" and \
       "hideGenerationTimestamp" not in ctx.attr.additional_properties:
        additional_properties["hideGenerationTimestamp"] = "true"

    #gen_cmd += " 2>/dev/null"
    return gen_cmd

def _impl(ctx):
    jars = _collect_jars(ctx.attr.deps)
    (cjars, rjars) = (jars.compiletime, jars.runtime)
    gen_dir = "{dirname}/{rule_name}".format(
        dirname = ctx.file.spec.dirname,
        rule_name = ctx.attr.name,
    )

    commands = [
        "mkdir -p {gen_dir}".format(
            gen_dir = gen_dir,
        ),
        _new_generator_command(ctx, gen_dir, rjars),
        "find {gen_dir} -exec touch -t 198001010000 {{}} \\;".format(
            gen_dir = gen_dir,
        ),
        "{jar} cMf {target} -C {srcs} .".format(
            jar = "%s/bin/jar" % ctx.attr._jdk[java_common.JavaRuntimeInfo].java_home,
            target = ctx.outputs.codegen.path,
            srcs = gen_dir,
        ),
    ]

    inputs = ctx.files._jdk + [
        ctx.file.codegen_cli,
        ctx.file.spec,
        ctx.file.config_file,
        ctx.file.logback_test,
    ] + _collect_files(ctx.attr.spec_refs) + cjars.to_list() + rjars.to_list()
    ctx.actions.run_shell(
        inputs = inputs,
        outputs = [ctx.actions.declare_directory("%s" % (ctx.attr.name)), ctx.outputs.codegen],
        command = " && ".join(commands),
        progress_message = "generating openapi sources %s" % ctx.label,
        arguments = [],
    )
    return struct(
        codegen = ctx.outputs.codegen,
    )

def _collect_jars(targets):
    """Compute the runtime and compile-time dependencies from the given targets"""  # noqa
    compile_jars = depset()
    runtime_jars = depset()
    for target in targets:
        found = False
        if hasattr(target, "scala"):
            if hasattr(target.scala.outputs, "ijar"):
                compile_jars = depset(transitive = [compile_jars, [target.scala.outputs.ijar]])
            compile_jars = depset(transitive = [compile_jars, target.scala.transitive_compile_exports])
            runtime_jars = depset(transitive = [runtime_jars, target.scala.transitive_runtime_deps])
            runtime_jars = depset(transitive = [runtime_jars, target.scala.transitive_runtime_exports])
            found = True
        if hasattr(target, "JavaInfo"):
            compile_jars = depset(transitive = [compile_jars, target[JavaInfo].transitive_deps])
            runtime_jars = depset(transitive = [runtime_jars, target[JavaInfo].transitive_runtime_deps])
            found = True
        if not found:
            runtime_jars = depset(transitive = [runtime_jars, target.files])
            compile_jars = depset(transitive = [compile_jars, target.files])

    return struct(compiletime = compile_jars, runtime = runtime_jars)

def _collect_files(targets):
    result = []
    for target in targets:
        result += target.files.to_list()
    return result

openapi_gen = rule(
    attrs = {
        "deps": attr.label_list(),
        "spec": attr.label(
            mandatory = True,
            allow_single_file = [".json", ".yaml"],
        ),
        "config_file": attr.label(
            allow_single_file = [".json"],
        ),
        "spec_refs": attr.label_list(
            allow_empty = True,
            allow_files = [".json", ".yaml"],
            default = [],
        ),
        "logback_test": attr.label(
            allow_single_file = [".xml"],
        ),
        "language": attr.string(
            default = "jaxrs-spec",
            mandatory = True,
        ),
        "output_dir": attr.string(),
        "api_package": attr.string(),
        "invoker_package": attr.string(),
        "model_package": attr.string(),
        "additional_properties": attr.string_dict(),
        "system_properties": attr.string_dict(),
        "type_mappings": attr.string_dict(),
        "import_mappings": attr.string(),
        "_jdk": attr.label(
            default = Label("@bazel_tools//tools/jdk:current_java_runtime"),
            providers = [java_common.JavaRuntimeInfo],
        ),
        "codegen_cli": attr.label(
            cfg = "host",
            default = Label("//external:io_bazel_rules_openapi/dependency/openapi-cli"),
            allow_single_file = True,
        ),
    },
    outputs = {
        "codegen": "%{name}_codegen.srcjar",
    },
    implementation = _impl,
)
