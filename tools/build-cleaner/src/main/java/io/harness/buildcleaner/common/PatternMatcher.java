/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.common;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
  /**
   * Parses content to find the single instance of group(1) inside matching pattern. If there are more than one
   * matching instances, it would throw runtime exception.
   *
   * @param pattern to match
   * @param content against which pattern is to be matched
   * @param filePath is just for logging purpose
   * @return if any match is found
   */
  public static Optional<String> findOnlyMatch(Pattern pattern, String content, Path filePath) {
    List<String> matches = findMatches(pattern, content);
    if (matches.size() > 1) {
      throw new RuntimeException(String.format(
          "Pattern '%s' matches more than one instance for fileName: '%s'", pattern, filePath.toString()));
    }

    if (matches.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(matches.get(0));
  }

  /**
   * Returns a list of matching group(1).
   *
   * @param pattern to match
   * @param content against which pattern is to be matched
   * @return list of matches
   */
  public static List<String> findMatches(Pattern pattern, String content) {
    Matcher matcher = pattern.matcher(content);
    List<String> matches = new ArrayList<>();

    while (matcher.find()) {
      matches.add(matcher.group(1));
    }
    return matches;
  }
}
