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
import lombok.Getter;

@Getter
public class JavaBinary {
  private final String name;
  private final String visibility;
  private final String mainClass;
  //  private ImmutableSortedSet<String> srcs;
  private final ImmutableSortedSet<String> runTimeDeps;
  private final ImmutableSortedSet<String> deps;
  private final Set<LoadStatement> loadStatements;

  public JavaBinary(final String name, final String visibility, final String mainClass, final Set<String> runTimeDeps,
      final Set<String> deps) {
    this.name = name;
    this.visibility = visibility;
    this.mainClass = mainClass;
    this.runTimeDeps = ImmutableSortedSet.copyOf(runTimeDeps);
    this.deps = ImmutableSortedSet.copyOf(deps);
    this.loadStatements = ImmutableSet.of(new LoadStatement("@rules_java//java:defs.bzl", "java_binary"));
  }

  /* Returns the deps section as a s string. Eg:
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

  /* Returns the runtime_deps section as a s string. Eg:
  runtime_deps = [
    "dependency1",
    "dependency2",
  ]
*/
  public String getRunTimeDepsSection() {
    StringBuilder response = new StringBuilder();
    WriteUtil.updateResponseWithSet(this.runTimeDeps, "runtime_deps", response, false);
    return response.toString();
  }

  public String toStarlark() {
    final StringBuilder response = new StringBuilder();
    response.append("java_binary(\n");

    // Add name.
    response.append(INDENTATION).append("name = \"").append(name).append("\",\n");

    // Add main_class.
    response.append(INDENTATION).append("main_class = \"").append(mainClass).append("\",\n");

    // Add visibility.
    response.append(INDENTATION).append("visibility = [\"//visibility:public\"],\n");

    // Add runtime_deps.
    WriteUtil.updateResponseWithSet(runTimeDeps, "runtime_deps", response, true);

    // Add deps.
    WriteUtil.updateResponseWithSet(deps, "deps", response, true);

    response.append(")");
    return response.toString();
  }
}
