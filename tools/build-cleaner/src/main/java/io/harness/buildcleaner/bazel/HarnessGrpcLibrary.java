/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.bazel;

import io.harness.buildcleaner.common.PatternMatcher;

import com.google.common.collect.ImmutableSortedSet;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class HarnessGrpcLibrary {
  private static final Pattern NAME_EXTRACTION_PATTERN =
      Pattern.compile("^^harness_grpc_library\\(\\s*name = \\\"(\\S*)\\\"", Pattern.MULTILINE);
  private static final Pattern DEPS_EXTRACTION_PATTERN =
      Pattern.compile("^harness_grpc_library\\([\\s\\S]*?deps = \\[([\\s\\S]*?)\\]", Pattern.MULTILINE);

  private final String name;
  private final ImmutableSortedSet<String> deps;

  private HarnessGrpcLibrary(String name, Set<String> deps) {
    this.name = name;
    this.deps = ImmutableSortedSet.copyOf(deps);
  }

  /**
   * Parses content to extract name and dependencies from BUILD file.
   * @param content containing BUILD file
   * @param filePath for logging purpose only
   * @return Parsed HarnessGrpcLibrary - if none exists, return empty optional
   */
  public static Optional<HarnessGrpcLibrary> parseFromString(String content, Path filePath) {
    Optional<String> harnessGrpcLibraryName = PatternMatcher.findOnlyMatch(NAME_EXTRACTION_PATTERN, content, filePath);
    if (!harnessGrpcLibraryName.isPresent()) {
      return Optional.empty();
    }

    Set<String> deps = parseDepsFromLibrary(content);
    return Optional.of(new HarnessGrpcLibrary(harnessGrpcLibraryName.get(), deps));
  }

  private static Set<String> parseDepsFromLibrary(String content) {
    Set<String> result = new HashSet<>();

    Matcher matcher = DEPS_EXTRACTION_PATTERN.matcher(content);
    if (!matcher.find()) {
      return result;
    }

    String commaSeparatedDeps = matcher.group(1);

    // Parsed result contains a comma separated list of dependencies. Split, trim and remove enclosing parenthesis
    // before returning.
    result.addAll(Arrays.stream(commaSeparatedDeps.split(","))
                      .map(str -> str.trim())
                      .filter(dependency -> !dependency.isEmpty())
                      .map(str -> str.substring(1, str.length() - 1))
                      .collect(Collectors.toSet()));

    return result;
  }
}
