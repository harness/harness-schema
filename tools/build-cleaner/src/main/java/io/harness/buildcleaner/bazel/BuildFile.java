/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.bazel;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BuildFile {
  private static final Pattern JAVA_BINARY_DEPS_PATTERN =
      Pattern.compile("java_binary\\([\\s\\S]*?(deps = \\[[\\s\\S]*?]),?", Pattern.MULTILINE);
  private static final Pattern JAVA_BINARY_RUNTIME_DEPS_PATTERN =
      Pattern.compile("java_binary\\([\\s\\S]*?(runtime_deps = \\[[\\s\\S]*?]),?", Pattern.MULTILINE);
  private static final Pattern JAVA_LIBRARY_DEPS_PATTERN =
      Pattern.compile("java_library\\([\\s\\S]*?(module|tests)[\\s\\S]*?(deps = \\[[\\s\\S]*?]),?", Pattern.MULTILINE);
  private static final Pattern JAVA_LIBRARY_RUNTIME_DEPS_PATTERN = Pattern.compile(
      "java_library\\([\\s\\S]*?(module|tests)[\\s\\S]*?(runtime_deps = \\[[\\s\\S]*?]),?", Pattern.MULTILINE);
  private static final int DEPS_CAPTURING_GROUP = 2;
  private static final int BINARY_DEPS_CAPTURING_GROUP = 1;

  private final SortedSet<LoadStatement> loadStatements = new TreeSet<>();
  private final List<JavaLibrary> javaLibraries = new ArrayList<>();
  private final List<JavaBinary> javaBinaries = new ArrayList<>();
  private boolean runAnalysisPerModule = false;

  public void addJavaLibrary(final JavaLibrary javaLibrary) {
    loadStatements.addAll(javaLibrary.getLoadStatements());
    javaLibraries.add(javaLibrary);
  }
  public void addJavaBinary(final JavaBinary javaBinary) {
    loadStatements.addAll(javaBinary.getLoadStatements());
    javaBinaries.add(javaBinary);
  }

  public void enableAnalysisPerModule() {
    loadStatements.add(new LoadStatement("//:tools/bazel/macros.bzl", "run_analysis_per_module"));
    runAnalysisPerModule = true;
  }

  /* Given a target name get all the dependencies */
  public String getDependencies(String targetName) {
    for (JavaLibrary javaLibrary : javaLibraries) {
      if (javaLibrary.getName().equalsIgnoreCase(targetName)) {
        return javaLibrary.getDepsSection();
      }
    }
    return "";
  }

  public String toStarlark() {
    final StringBuilder response = new StringBuilder();
    loadStatements.forEach(statement -> response.append(statement.toStarlark()).append("\n"));
    response.append("\n");

    javaLibraries.forEach(library -> response.append(library.toStarlark()).append("\n"));
    response.append("\n");

    javaBinaries.forEach(binary -> response.append(binary.toStarlark()).append("\n"));

    if (runAnalysisPerModule) {
      response.append("run_analysis_per_module(");
      if (javaLibraries.stream().allMatch(JavaLibrary::isTest)) {
        response.append("test_only = True");
      }
      response.append(")").append("\n");
    }

    javaLibraries.stream()
        .filter(JavaLibrary::isTest)
        .forEach(library
            -> response.append("\n")
                   .append("run_tests(\"")
                   .append(normalizeRunTestSrcGlob(library.getSrcsGlob()))
                   .append("\")\n"));

    return response.toString();
  }

  // Run test srcs glob must end with Test.java (so that run_tests macro only create java_test targets for actual tests)
  private String normalizeRunTestSrcGlob(final String srcsGlob) {
    if (srcsGlob.endsWith("Test.java"))
      return srcsGlob;
    return srcsGlob.replace(".java", "Test.java");
  }

  public void writeToPackage(final Path directory) throws FileNotFoundException {
    try (final PrintWriter out = new PrintWriter(directory + "/BUILD.bazel")) {
      out.print(this.toStarlark());
    } catch (FileNotFoundException ex) {
      throw new RuntimeException("Could not write out build file to: " + directory + "/BUILD.bazel");
    }
  }

  // Update deps and runtime_deps in a BUILD.bazel file.
  public void updateDependencies(Path buildFilePath) throws IOException {
    updateDepsHelper(buildFilePath, "java_library", "deps");
    updateDepsHelper(buildFilePath, "java_binary", "deps");
    updateDepsHelper(buildFilePath, "java_binary", "runtime_deps");
  }

  private String getNewLibraryDeps(String updateField) {
    String newDeps = String.format("%s = []", updateField);
    for (final JavaLibrary javaLibrary : getJavaLibraries()) {
      log.debug("Library Name: {}", javaLibrary.getName());
      switch (updateField) {
        case "deps":
          newDeps = javaLibrary.getDepsSection();
          break;
        case "runtime_deps":
          newDeps = javaLibrary.getRunTimeDepsSection();
          break;
      }
    }
    log.debug("New {} for java_library: {}", updateField, newDeps);
    return newDeps;
  }

  private String getNewBinaryDeps(String updateField) {
    String newDeps = String.format("%s = []", updateField);
    for (final JavaBinary javaBinary : getJavaBinaries()) {
      log.debug("Binary Name: {}", javaBinary.getName());
      switch (updateField) {
        case "deps":
          newDeps = javaBinary.getDepsSection();
          break;
        case "runtime_deps":
          newDeps = javaBinary.getRunTimeDepsSection();
          break;
      }
    }
    log.debug("New {} for java_binary: {}", updateField, newDeps);
    return newDeps;
  }

  // Updates BUILD.bazel file's specified buildRule and updateField
  // buildRule: Accepted values are "java_library" and "java_binary"
  // updateField: Accepted values are "deps" and "runtime_deps"
  private void updateDepsHelper(Path buildFilePath, String buildRule, String updateField) throws IOException {
    // This will contain the latest values which should be updated in the file.
    String newDeps;
    Pattern pattern = null;
    final int depsCapturingGroup;
    switch (buildRule) {
      case "java_library":
        newDeps = getNewLibraryDeps(updateField);
        if (updateField.equalsIgnoreCase("deps")) {
          pattern = JAVA_LIBRARY_DEPS_PATTERN;
        } else if (updateField.equalsIgnoreCase("runtime_deps")) {
          pattern = JAVA_LIBRARY_RUNTIME_DEPS_PATTERN;
        }
        depsCapturingGroup = DEPS_CAPTURING_GROUP;
        break;
      case "java_binary":
        newDeps = getNewBinaryDeps(updateField);
        if (updateField.equalsIgnoreCase("deps")) {
          pattern = JAVA_BINARY_DEPS_PATTERN;
        } else if (updateField.equalsIgnoreCase("runtime_deps")) {
          pattern = JAVA_BINARY_RUNTIME_DEPS_PATTERN;
        }
        depsCapturingGroup = BINARY_DEPS_CAPTURING_GROUP;
        break;
      default:
        log.warn("Unsupported buildRule: {}", buildRule);
        return;
    }

    if (pattern == null) {
      return;
    }

    String currContent = Files.readString(buildFilePath);
    log.trace("Current file content: \n {}", currContent);
    Matcher matcher = pattern.matcher(currContent);
    StringBuilder builder = new StringBuilder();
    builder.append(currContent);
    String replacedFileContent = "";
    while (matcher.find()) {
      String textToReplace = matcher.group(depsCapturingGroup);
      log.trace("Found text to replace: {}", textToReplace);
      replacedFileContent =
          builder.replace(matcher.start(depsCapturingGroup), matcher.end(depsCapturingGroup), newDeps).toString();
      log.trace("Replaced file content: {}", replacedFileContent);
    }
    if (!replacedFileContent.equalsIgnoreCase("")) {
      // Overwrite a file only if we are able to replace.
      Files.writeString(buildFilePath, replacedFileContent, TRUNCATE_EXISTING);
    }
  }
}