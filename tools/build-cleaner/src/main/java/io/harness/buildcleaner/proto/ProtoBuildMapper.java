/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.proto;

import io.harness.buildcleaner.bazel.HarnessGrpcLibrary;
import io.harness.buildcleaner.common.PatternMatcher;
import io.harness.buildcleaner.common.SymbolDependencyMap;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoBuildMapper {
  private static final Pattern PROTO_LIBRARY_PATTERN =
      Pattern.compile("^proto_library\\(\\s*name = \\\"(\\S*)\\\"", Pattern.MULTILINE);

  private final HashMap<String, String> protoMessageToProtoFolder = new HashMap<>();
  private final HashMap<String, String> protoFolderToProtoLibrary = new HashMap<>();
  private final HashMap<String, String> protoLibraryToHarnessGrpcLibrary = new HashMap();

  private final Path workspace;

  public ProtoBuildMapper(Path workspace) {
    this.workspace = workspace;
  }

  /**
   * Parse proto in the given path. In addition to the proto files, we would also be parsing BUILD files to look for
   * existing proto target names for the given proto files. Unlike Java, we don't have a naming convention for the
   * proto library modules. Also, proto_java_libraries are not present in the same location as the proto files, which
   * makes it harder to generalize.
   *
   * This mapper searches BUILD files for proto_library and harness_grpc_library target names. harness_grpc_library
   * contains proto_library targets as dependencies. We assume that proto_library target would be present in the same
   * folder as the proto files.
   *
   * @param srcs matched with the input pattern.
   * @throws IOException
   */
  public void protoToBuildTargetDependencyMap(final String srcs, final SymbolDependencyMap protoSymbolToDependency)
      throws IOException {
    String pattern = Paths.get(workspace.toString(), srcs).toString();
    log.info("Parsing proto files: {}", pattern);

    PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    try (Stream<Path> paths = Files.find(workspace, Integer.MAX_VALUE, (path, f) -> pathMatcher.matches(path))) {
      paths.forEach(this::parseFile);
    }

    buildProtoSymbolToDependencyMap(protoSymbolToDependency);
  }

  private void parseFile(Path path) {
    try {
      if (path.toString().endsWith(".proto")) {
        parseProtoFile(path);
      } else if (path.toString().endsWith("BUILD.bazel")) {
        parseBuildFile(path);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private void parseProtoFile(Path path) throws IOException {
    ProtoFile protoFile = ProtoFileParser.parse(path);

    final Path effectiveBuildModulePath = workspace.relativize(path.getParent());
    protoFile.getJavaClassNames().stream().forEach(
        className -> addProtoMessageToProtoFolderMapping(className, effectiveBuildModulePath.toString()));
  }

  private void parseBuildFile(Path path) throws IOException {
    log.debug("Parsing BUILD file: {}", path);
    String content = new String(Files.readAllBytes(path));

    final Path effectiveBuildModulePath = workspace.relativize(path.getParent());

    Optional<String> protoLibraryName = PatternMatcher.findOnlyMatch(PROTO_LIBRARY_PATTERN, content, path);
    if (protoLibraryName.isPresent()) {
      protoFolderToProtoLibrary.put(effectiveBuildModulePath.toString(),
          String.format("//%s:%s", effectiveBuildModulePath.toString(), protoLibraryName.get()));
    }

    Optional<HarnessGrpcLibrary> harnessGrpcLibrary = HarnessGrpcLibrary.parseFromString(content, path);
    if (harnessGrpcLibrary.isPresent()) {
      for (String dependency : harnessGrpcLibrary.get().getDeps()) {
        protoLibraryToHarnessGrpcLibrary.put(dependency,
            String.format("//%s:%s", effectiveBuildModulePath.toString(), harnessGrpcLibrary.get().getName()));
      }
    }
  }

  /**
   * Constructs a symbol dependency map from class name corresponding to a Proto message to java target.
   */
  private void buildProtoSymbolToDependencyMap(final SymbolDependencyMap protoSymbolToDependency) {
    for (Map.Entry<String, String> entry : protoMessageToProtoFolder.entrySet()) {
      Optional<String> javaTarget = findJavaTargetForFolder(entry.getValue());
      javaTarget.ifPresent(target -> protoSymbolToDependency.addSymbolTarget(entry.getKey(), target));
    }
  }

  /**
   * This method assumes a certain directory structure in which proto files and its corresponding BUILD
   * files are organized.
   *
   * All proto files inside one folder would have a single proto_library target and then there would exist
   * a harness_grpc_library somewhere which would have proto_library as its dependency.
   * Another assumption is that proto_library target would have only one corresponding harness_grpc_library.
   *
   * @return name of the harness_grpc_library_target
   */
  private Optional<String> findJavaTargetForFolder(String folder) {
    if (!protoFolderToProtoLibrary.containsKey(folder)) {
      return Optional.empty();
    }
    String protoLibrary = protoFolderToProtoLibrary.get(folder);
    if (!protoLibraryToHarnessGrpcLibrary.containsKey(protoLibrary)) {
      return Optional.empty();
    }
    return Optional.of(protoLibraryToHarnessGrpcLibrary.get(protoLibrary));
  }

  private void addProtoMessageToProtoFolderMapping(String protoJavaClassName, String folder) {
    protoMessageToProtoFolder.put(protoJavaClassName, folder);
  }
}
