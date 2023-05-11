# Overview

Build cleaner is meant to generate (and eventually update) build files for any folder (top level or nested) in the
harness-core repository. It helps to resolve dependencies by statically analyzing the source files.

# Assumptions

There are certain assumptions baked into the tool:

* Each folder would have a BUILD file
* Each BUILD file would have one target (for java_library) covering all source files in that folder
* All java files in one folder should have the same package name

# Not Yet Supported

* Proto files
* Test targets
* Updating existing BUILD files
* Pseudo support for removing assumption - "Each folder would have a BUILD file".

# How to Execute

1) Generate Build file in `999-annotations/src/main/java/io/harness/agent/sdk`.
    1) --workspace: to specify the top level folder
    2) --module: to tell the name of folder for which to generate BUILD file

```
bazel run  tools/build-cleaner/src/main/java/io/harness/buildcleaner:build_cleaner -- --workspace=$PWD --module 999-annotations/src/main/java/io/harness/agent/sdk
```

2) Generate BUILD files for each sub-folder in 999-annotations with non-zero source files.
    1) --workspace: to specify the top level folder
    2) --module: inside which to generate BUILD files
    3) --recursive: to recursively generate BUILD files

```
bazel run  tools/build-cleaner/src/main/java/io/harness/buildcleaner:build_cleaner -- --workspace=$PWD --module 999-annotations/ --recursive=true
```

# Internals

This script runs in two phases:

1) Index generation
    1) We iterate over all the source files (as per the input regex pattern), and create a map of class name to folder
       path.
        1) This index is persisted in the file system locally - the next run of the tool would not re-generate this
           index.
        2) One can specify the index file as a command line argument too.
    2) For resolving the maven targets, we have checked-in ~/main-manifest.json file, which has mapping from java
       package to maven target.
        1) This file is generated using parse-jars script, which parses maven jars listed in the
       project/main_maven_install.json
       ```
       # Pin Maven deps
       bazel run @unpinned_maven//:pin
       
       # Download Maven deps so the Maven resolver can do its work
       bazel fetch @maven//...
       
       bazel run @contrib_rules_jvm//java/gazelle/cmd/parsejars -- -maven-install project/main_maven_install.json --repo-root "$PWD" --output maven-manifest.json

       ```
        2) There is also an option to override this mapping, by adding entries in ~/maven-manifest-override.txt file.
           This is required because certain packages are duplicated across maven targets.
2) Resolving imports and generating BUILD files
    1) We parse each source file and get a list of import statements.
        1) If import starts with "java.", we ignore those and no dependency is added.
        2) Else, we check the import in Harness symbol to path mapping and the maven manifest file.
    2) Sometimes, we have static imports, where we import nested symbols. We have a custom heuristic for this, which can
       evolve over time.
        1) We keep stripping off symbols from the right of the import statement, until we resolve it. A nested symbol
           would not be found in our mappings, but it's enclosing class should always be found.
    3) Once targets are resolved, tool would generate java_library or java_binary targets, depending upon the presence
       of main methods in java files.