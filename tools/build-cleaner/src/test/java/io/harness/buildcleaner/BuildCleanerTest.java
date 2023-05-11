/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import io.harness.buildcleaner.bazel.BuildFile;
import io.harness.buildcleaner.bazel.JavaLibrary;
import io.harness.buildcleaner.bazel.LoadStatement;
import io.harness.buildcleaner.common.SymbolDependencyMap;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BuildCleanerTest {
  private static final String PACKAGE_ROOT = "io.harness";

  @Rule public TemporaryFolder workspace = new TemporaryFolder();
  private static final String CLASS_PATTERN =
      new StringBuilder().append("public class %s {").append(System.lineSeparator()).append("}").toString();

  @Test
  public void generateIndex_singleDirectory_generatesHarnessSymbolMap() throws IOException, ClassNotFoundException {
    // Arrange
    writeClassToFile(workspace.getRoot(), "A1.java");
    writeClassToFile(workspace.getRoot(), "A2.java");

    BuildCleaner buildCleaner =
        new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString(), "--indexSourceGlob", "*.java"});

    // Act & Assert
    assertThat(buildCleaner.buildHarnessSymbolMap().getSymbolToTargetMap())
        .isEqualTo(Map.of("io.harness.A1", "", "io.harness.A2", ""));
  }

  @Test
  public void generateIndex_nestedDirectories_generatesHarnessSymbolMap() throws IOException, ClassNotFoundException {
    // Arrange
    writeClassToFile(workspace.getRoot(), "Root.java");

    File nestedFolder = workspace.newFolder("nested");
    writeClassToFile(nestedFolder, "Nested.java");

    BuildCleaner buildCleaner =
        new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString(), "--indexSourceGlob", "**"});

    // Act & Assert
    assertThat(buildCleaner.buildHarnessSymbolMap().getSymbolToTargetMap())
        .isEqualTo(Map.of("io.harness.Root", "", "io.harness.nested.Nested", "nested"));
  }

  @Test
  public void generateIndex_findBuildInParentFolder_generatesHarnessSymbolMap()
      throws IOException, ClassNotFoundException {
    // Arrange
    writeClassToFile(workspace.getRoot(), "Root.java");
    createEmptyFile(workspace.getRoot(), "BUILD.bazel");

    File nestedFolder = workspace.newFolder("nested");
    writeClassToFile(nestedFolder, "Nested.java");

    BuildCleaner buildCleaner =
        new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString(), "--indexSourceGlob", "**"});

    // Act & Assert
    assertThat(buildCleaner.buildHarnessSymbolMap().getSymbolToTargetMap())
        .isEqualTo(Map.of("io.harness.Root", "", "io.harness.nested.Nested", ""));
  }

  @Test
  public void generateIndex_withAssumedPackagePrefixes_doesNotFindInParentFolder()
      throws IOException, ClassNotFoundException {
    // Arrange
    writeClassToFile(workspace.getRoot(), "Root.java");
    createEmptyFile(workspace.getRoot(), "BUILD.bazel");

    File nestedFolder = workspace.newFolder("nested");
    writeClassToFile(nestedFolder, "Nested.java");

    BuildCleaner buildCleaner = new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString(),
        "--indexSourceGlob", "**", "--assumedPackagePrefixesWithBuildFile", "nested"});

    // Act & Assert
    assertThat(buildCleaner.buildHarnessSymbolMap().getSymbolToTargetMap())
        .isEqualTo(Map.of("io.harness.Root", "", "io.harness.nested.Nested", "nested"));
  }

  @Test
  public void generateIndex_withProtoFiles_mapsProtoClassToHarnessGrpcTarget()
      throws IOException, ClassNotFoundException {
    // Arrange

    // Create proto file.
    String protoFileContent = new StringBuilder()
                                  .append("package io.harness.test;\n")
                                  .append("message TestProto {\n")
                                  .append("  int64 val = 1;\n")
                                  .append("}")
                                  .toString();
    writeFileWithContent(workspace.getRoot(), "test.proto", protoFileContent);

    // Create BUILD file with proto_library and harness_grpc_library targets.
    String buildFileContent = new StringBuilder()
                                  .append("proto_library(\n")
                                  .append("    name = \"100_test_proto\",")
                                  .append(")\n")
                                  .append("harness_grpc_library(\n")
                                  .append("    name = \"100_test_java_proto\",")
                                  .append("    deps = [")
                                  .append("        \"//:100_test_proto\",")
                                  .append("    ],")
                                  .append(")\n")
                                  .toString();
    writeFileWithContent(workspace.getRoot(), "BUILD.bazel", buildFileContent);

    BuildCleaner buildCleaner =
        new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString(), "--indexSourceGlob", "**"});

    // Act & Assert
    assertThat(buildCleaner.buildHarnessSymbolMap().getSymbolToTargetMap())
        .isEqualTo(Map.of("io.harness.test.TestProto", "//:100_test_java_proto"));
  }

  @Test
  public void generateBuildForModule_oneSourceFile_allImportsInSymbolMap_returnBuildWithDeps() throws IOException {
    // Arrange
    List<String> imports = List.of("io.harness.RootClass", "io.harness.nested.NestedClass");
    writeClassToFile(workspace.getRoot(), "File.java", imports);

    BuildCleaner buildCleaner = new BuildCleaner(new String[] {"--workspace", workspace.getRoot().toString()});

    SymbolDependencyMap indexedDependencyMap = new SymbolDependencyMap();
    indexedDependencyMap.addSymbolTarget("io.harness.RootClass", "root");
    indexedDependencyMap.addSymbolTarget("io.harness.nested.NestedClass", "root/nested");

    // Act
    BuildFile outputBuildFile = buildCleaner.generateBuildForModule(Paths.get(""), indexedDependencyMap).get();

    // Assert
    assertTrue(outputBuildFile.getJavaBinaries().isEmpty());

    SortedSet<LoadStatement> loadStatementSet = outputBuildFile.getLoadStatements();
    assertEquals(2, loadStatementSet.size());
    assertEquals("load(\"@rules_java//java:defs.bzl\", \"java_library\")",
        loadStatementSet.stream().findFirst().get().toStarlark());

    List<JavaLibrary> javaLibraryTargets = outputBuildFile.getJavaLibraries();
    assertEquals(1, javaLibraryTargets.size());
    assertEquals(ImmutableSet.of("//root:module", "//root/nested:module"), javaLibraryTargets.get(0).getDeps());
  }

  private void writeClassToFile(File folder, String fileName) throws IOException {
    writeClassToFile(folder, fileName, Collections.emptyList());
  }

  private void writeClassToFile(File folder, String fileName, List<String> imports) throws IOException {
    File file = new File(folder + "/" + fileName);
    file.createNewFile();

    String packageName = Paths.get(workspace.getRoot().getPath()).relativize(Paths.get(folder.getPath())).toString();
    if (packageName.isEmpty()) {
      packageName = PACKAGE_ROOT;
    } else {
      packageName = PACKAGE_ROOT + "." + packageName;
    }

    String content = String.format(CLASS_PATTERN, fileName.substring(0, fileName.indexOf(".")));

    try (PrintWriter out = new PrintWriter(file);) {
      out.println(String.format("package %s;", packageName));
      for (String packageToImport : imports) {
        out.println(String.format("import %s;", packageToImport));
      }
      out.println(content);
      out.flush();
    }
  }

  private void createEmptyFile(File folder, String fileName) throws IOException {
    File file = new File(folder + "/" + fileName);
    file.createNewFile();
  }

  private void writeFileWithContent(File folder, String fileName, String content) throws IOException {
    File file = new File(folder + "/" + fileName);
    file.createNewFile();

    try (PrintWriter out = new PrintWriter(file);) {
      out.println(content);
      out.flush();
    }
  }
}
