load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "pmd",
    main_class = "net.sourceforge.pmd.PMD",
    visibility = ["//visibility:public"],
    runtime_deps = ["@rules_pmd_dependencies//:net_sourceforge_pmd_pmd_dist"],
)
