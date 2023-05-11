/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.proto;

import static io.harness.buildcleaner.common.PatternMatcher.findMatches;
import static io.harness.buildcleaner.common.PatternMatcher.findOnlyMatch;

import io.harness.buildcleaner.proto.ProtoFile.ProtoFileBuilder;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProtoFileParser {
  private static final Pattern PACKAGE_NAME_PATTERN = Pattern.compile("^package (.+);", Pattern.MULTILINE);
  private static final Pattern IMPORTS_PATTERN = Pattern.compile("^import (.+);", Pattern.MULTILINE);
  private static final Pattern JAVA_PACKAGE_PATTERN =
      Pattern.compile("^option.*java_package.*=.*\"(.+)\";", Pattern.MULTILINE);
  private static final Pattern JAVA_OUTER_CLASS_NAME_PATTERN =
      Pattern.compile("^option.*java_outer_classname.*=.*\"(.+)\";", Pattern.MULTILINE);
  private static final Pattern MESSAGE_NAME_PATTERN = Pattern.compile("^message\\s+(\\w+)\\s*\\{", Pattern.MULTILINE);

  public static ProtoFile parse(Path filePath) throws IOException {
    String content = new String(Files.readAllBytes(filePath));

    // Read the package name.
    Optional<String> packageName = findOnlyMatch(PACKAGE_NAME_PATTERN, content, filePath);
    if (!packageName.isPresent()) {
      throw new RuntimeException(String.format("Proto file '%s' has no package name.", filePath.toString()));
    }

    // Read all import statements.
    List<String> importStatements = findMatches(IMPORTS_PATTERN, content);

    // Read proto messages.
    List<String> messageNames = findMatches(MESSAGE_NAME_PATTERN, content);

    ProtoFileBuilder protoFile = ProtoFile.builder(
        packageName.get(), ImmutableList.copyOf(importStatements), ImmutableList.copyOf(messageNames));

    // Java package
    Optional<String> javaPackage = findOnlyMatch(JAVA_PACKAGE_PATTERN, content, filePath);
    javaPackage.ifPresent(protoFile::javaPackage);

    // Java outer classname
    Optional<String> javaOuterClassName = findOnlyMatch(JAVA_OUTER_CLASS_NAME_PATTERN, content, filePath);
    javaOuterClassName.ifPresent(protoFile::javaOuterClassName);

    return protoFile.build();
  }
}
