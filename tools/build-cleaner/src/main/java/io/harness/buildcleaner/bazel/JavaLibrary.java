/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.bazel;

import static io.harness.buildcleaner.bazel.WriteUtil.INDENTATION;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class JavaLibrary {
  // For test targets srcs glob will usually either be srcs/test/**/*.java or *Test.java
  private static final Pattern TEST_SRCS_GLOB = Pattern.compile("test", Pattern.CASE_INSENSITIVE);
  // Having JUnit as a dep is another good indicator we are working with test target
  private static final Pattern HAS_JUNIT = Pattern.compile("junit", Pattern.CASE_INSENSITIVE);
  // Our test macros require java_library name to be tests
  private static final String DEFAULT_TESTS_NAME = "tests";
  private final String name;
  private final String visibility;
  private final String srcsGlob;
  private final boolean isTest;
  private final Set<LoadStatement> loadStatements;

  private final ImmutableSortedSet<String> runTimeDeps;
  private final ImmutableSortedSet<String> deps;

  public JavaLibrary(final String name, final String visibility, final String srcsGlob, final Set<String> deps) {
    this.visibility = visibility;
    this.srcsGlob = srcsGlob;
    this.deps = ImmutableSortedSet.copyOf(deps);
    this.runTimeDeps = ImmutableSortedSet.of();
    // isTest condition is not foolproof but its good enough for now.
    // It will usually short-circuit, so normally we don't need to go through deps.
    this.isTest =
        TEST_SRCS_GLOB.matcher(srcsGlob).find() || deps.stream().anyMatch(dep -> HAS_JUNIT.matcher(dep).find());
    final var loadStatementsBuilder =
        ImmutableSet.<LoadStatement>builder().add(new LoadStatement("@rules_java//java:defs.bzl", "java_library"));
    if (isTest) {
      loadStatementsBuilder.add(new LoadStatement("@rules_java//java:defs.bzl", "java_library"));
    }
    //    this.loadStatements = ImmutableSet.of(new LoadStatement("@rules_java//java:defs.bzl", "java_library"),
    //        new LoadStatement("//:tools/bazel/GenTestRules.bzl", "run_tests"));
    this.loadStatements = loadStatementsBuilder.build();
    this.name = isTest ? DEFAULT_TESTS_NAME : name;
  }

  /* Returns the deps section as a string. Eg:
    deps = [
      "dependency1",
      "dependency2",
    ]
  */
  public String getDepsSection() {
    StringBuilder response = new StringBuilder();
    WriteUtil.updateResponseWithSet(this.deps, "deps", response, false);
    return response.toString();
  }

  public String getRunTimeDepsSection() {
    StringBuilder response = new StringBuilder();
    WriteUtil.updateResponseWithSet(this.runTimeDeps, "runtime_deps", response, false);
    return response.toString();
  }

  public String toStarlark() {
    final StringBuilder response = new StringBuilder();
    response.append("java_library(\n");

    // Add name.
    response.append(INDENTATION).append("name = \"").append(name).append("\",\n");

    // Add testonly.
    if (isTest) {
      response.append(INDENTATION).append("testonly = True,\n");
    }

    // Add srcs.
    response.append(INDENTATION).append("srcs = glob([\"").append(srcsGlob).append("\"]),\n");

    // Add visibility.
    response.append(INDENTATION).append("visibility = [\"//visibility:public\"],\n");

    // Add deps.
    WriteUtil.updateResponseWithSet(deps, "deps", response, true);

    response.append(")");
    return response.toString();
  }
}
