# Copyright 2022 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

# ==== Aspect implementation ====

def _brannock_aspect_impl(target, ctx):
    name = target.label.name.replace("/", "").replace(":", "!")

    out_files = []
    for tar in target.files.to_list():
        out_files.append(_get_size(file = tar, ctx = ctx))

    return [OutputGroupInfo(size = depset(
        out_files,
        transitive = [dep[OutputGroupInfo].size for dep in ctx.rule.attr.deps],
    ))]

brannock_aspect = aspect(
    implementation = _brannock_aspect_impl,
    attr_aspects = ["deps"],
)

def _get_size(file, ctx):
    in_file = file
    out_file = ctx.actions.declare_file("%s.size" % file.basename)

    # This shell command will take one file (actual dependency jar).
    # It will out it's size in bytes in first column and target name in second. Final file will only have one line
    # 1. wc -c '%s' ... gets size in bytes
    # 2. ... (index(\"%s\", \"maven\")?\"@\":\"\") \"%s//%s:%s\" ... adds target name, prepending maven targets with @ so they have expected name
    ctx.actions.run_shell(
        inputs = [in_file],
        outputs = [out_file],
        progress_message = "Getting size of %s" % in_file.short_path,
        command = "wc -c '%s' | awk '{print $1 \"\t\" (index(\"%s\", \"maven\")?\"@\":\"\") \"%s//%s:%s\"}' > '%s'" % (in_file.path, in_file.owner.workspace_name, in_file.owner.workspace_name, in_file.owner.package, in_file.owner.name, out_file.path),
    )

    return out_file

# ==== Rule implementation which uses the above aspect ====

def _brannock_rule_impl(ctx):
    sizes = []
    dep_sizes = []
    for dep in ctx.attr.deps:
        dep_sizes.extend([_sum_dep_size(dep, ctx)])
        sizes.extend(dep[OutputGroupInfo].size.to_list())

    tmp_out = ctx.actions.declare_file("%s.tmp" % ctx.outputs.out.basename)
    out = ctx.outputs.out
    _concat_size_files(sizes, tmp_out, ctx)
    _convert_units(tmp_out, out, ctx)

    out_dep_sizes = ctx.actions.declare_file("%s.deps" % ctx.outputs.out.basename)
    _concat_size_files(dep_sizes, out_dep_sizes, ctx)

    return [OutputGroupInfo(size = depset([out, out_dep_sizes]))]

brannock_rule = rule(
    implementation = _brannock_rule_impl,
    attrs = {
        "out": attr.output(mandatory = True),
        "deps": attr.label_list(aspects = [brannock_aspect]),
    },
)

def _convert_units(in_file, out_file, ctx):
    # This shell command will take one file with many sizes in bytes in first column and target in second column.
    # It will create output file with same data, but sizes will be in human readable format
    # 1. ... split( \"B KB MB\" , v ); s=1; while( sum>1024 ){ sum/=1024; s++ } ... converts to human readable format (B, KB, MB)
    ctx.actions.run_shell(
        inputs = [in_file],
        outputs = [out_file],
        command = "cat %s | awk '{ split( \"B KB MB\" , v ); s=1; while( $1>1024 ){ $1/=1024; s++ } print int($1)v[s] \"\t\" $2 }' > %s" % (in_file.path, out_file.path),
    )
    return out_file

def _sum_dep_size(dep, ctx):
    tmp_file = ctx.actions.declare_file("%s_%s.tmp" % (dep.label.package, dep.label.name))
    out_file = ctx.actions.declare_file("%s_%s.totalsize" % (dep.label.package, dep.label.name))

    # We want only one line per dep, so concatenate all dep size reports into a single one
    _concat_size_files(dep[OutputGroupInfo].size.to_list(), tmp_file, ctx)

    # This shell command will take one file with many sizes in bytes, one per line (second column here is target name for each size, but we ignore it).
    # It will create output file which will sum up all values, convert to human readable and add target name
    # 1. cat %s | sort | uniq | awk '{ sum += $1 } ... takes files and sums values
    # 2. ... split( \"B KB MB\" , v ); s=1; while( sum>1024 ){ sum/=1024; s++ } ... converts to human readable format (B, KB, MB)
    # 3. ... (index(\"%s\", \"maven\")?\"@\":\"\") \"%s//%s:%s\" ... adds target name, prepending maven targets with @ so they have expected name
    ctx.actions.run_shell(
        inputs = [tmp_file],
        outputs = [out_file],
        command = "cat %s | sort | uniq | awk '{ sum += $1 } END { split( \"B KB MB\" , v ); s=1; while( sum>1024 ){ sum/=1024; s++ } print int(sum)v[s] \"\t\" (index(\"%s\", \"maven\")?\"@\":\"\") \"%s//%s:%s\"}' > %s" % (tmp_file.path, dep.label.workspace_name, dep.label.workspace_name, dep.label.package, dep.label.name, out_file.path),
    )
    return out_file

def _concat_size_files(sizes, out, ctx):
    # depset guarantees uniqueness, this is to trim down duplicate files
    uniq_list = depset(sizes).to_list()

    cmd = "cat {srcs} | sort -rh >> {out}".format(
        srcs = " ".join([p.path for p in uniq_list]),
        out = out.path,
    )

    ctx.actions.run_shell(
        inputs = uniq_list,
        outputs = [out],
        command = cmd,
    )
    return out
