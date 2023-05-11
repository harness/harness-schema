/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathParser {
  private static final Logger logger = LoggerFactory.getLogger(ClasspathParser.class);

  private final Set<String> usedTypes = new TreeSet<>();
  private final Set<String> packages = new TreeSet<>();
  private final Set<String> mainClasses = new TreeSet<>();
  private final Set<ClassMetadata> classMetadatas = new HashSet<>();
  private final JavaParser javaParser;
  private final Path workspace;

  public ClasspathParser(JavaParser javaParser, Path workspace) {
    this.javaParser = javaParser;
    this.workspace = workspace;
  }

  public Set<String> getUsedTypes() {
    return usedTypes;
  }

  public Set<String> getPackages() {
    return packages;
  }

  public Set<String> getMainClasses() {
    return mainClasses;
  }

  public Set<ClassMetadata> getFullyQualifiedClassNames() {
    return classMetadatas;
  }

  public void parseClasses(String srcs, Set<String> assumedPackagePrefixesWithBuildFile) throws IOException {
    String pattern = workspace.toString() + "/" + srcs;
    logger.info("Parsing java files: {}", pattern);
    PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    try (Stream<Path> paths = Files.find(workspace, Integer.MAX_VALUE, (path, f) -> pathMatcher.matches(path))) {
      paths.peek(path -> logger.debug("processing {}", path)).forEach(path -> {
        if (path.toString().endsWith(".java")) {
          parseFileGatherDependencies(path, assumedPackagePrefixesWithBuildFile);
        }
      });
    }
  }

  public void parseClasses(Path directory, List<String> files) {
    files.stream()
        .map(filename -> workspace.resolve(directory).resolve(filename))
        .forEach(this::parseFileGatherDependencies);
  }

  private void parseFileGatherDependencies(Path classPath) {
    parseFileGatherDependencies(classPath, new HashSet<String>());
  }

  private void parseFileGatherDependencies(Path classPath, Set<String> assumedPackagePrefixesWithBuildFile) {
    CompilationUnit cu;
    try {
      ParseResult<CompilationUnit> result = this.javaParser.parse(classPath);
      if (result.isSuccessful()) {
        cu = result.getResult().get();
      } else {
        logger.error("Failed parsing a path {}. The issue was {}", classPath, result.getProblems());
        throw new ParseProblemException(result.getProblems());
      }
    } catch (IOException exception) {
      logger.warn("Unable to parse {}. Skipping File", classPath);
      return;
    }
    parseImports(cu);
    parseClasses(cu);
    parsePackages(cu);
    findMainMethods(cu);
    parseFullyQualifiedClassNames(classPath, cu, assumedPackagePrefixesWithBuildFile);
  }

  private void parseImports(CompilationUnit cu) {
    // IMPORTS : Checking the imports
    cu.findAll(ImportDeclaration.class).forEach(id -> {
      String name = id.getNameAsString();
      if (id.isAsterisk()) {
        logger.debug("Handling wildcard import: {} to package {}", id, name);
        usedTypes.add(name);
      } else if (id.isStatic()) {
        String staticPackage = name.substring(0, name.lastIndexOf('.'));
        usedTypes.add(staticPackage);
      } else {
        usedTypes.add(name);
      }
    });
  }

  private void parseClasses(CompilationUnit cu) {
    Set<ClassOrInterfaceType> visited = new HashSet<>();

    // CLASSES : Checking the fully qualified class or interface names
    cu.findAll(ClassOrInterfaceType.class).forEach(coit -> {
      if (visited.contains(coit)) {
        return;
      }
      ResolvedReferenceTypeDeclaration type;
      String typeName = null;
      if (!Character.isUpperCase(coit.getName().asString().charAt(0))) {
        logger.trace("Working on {} and thinking this is a package, so skipping", coit.getName().asString());
      } else if (coit.isArrayType()) {
        ArrayType arrayTyoe = coit.asArrayType();
        type = arrayTyoe.resolve().getComponentType().asReferenceType().getTypeDeclaration().get();
        typeName = type.getQualifiedName();
      } else {
        try {
          ResolvedReferenceType ref = coit.resolve();
          type = ref.getTypeDeclaration().get();
          typeName = type.getQualifiedName();
        } catch (UnsolvedSymbolException exception) {
          logger.trace("Working on class {} And unable to find: {} -"
                  + " Continuing",
              cu.getPrimaryTypeName().get(), exception.getName());
        } catch (UnsupportedOperationException exception) {
          // The ResolvedReferenceType is the generics for some classes,
          // which the system
          // can not resolve
          // begin generic. We're not alerting on this, but skipping and
          // assuming we don't
          // need to resolve
          // the generic type reference by applying a dependency.
          if (exception.getMessage().contains("CorrespondingDeclaration")) {
            logger.debug("Working on classes from {} - {}\n"
                    + " unable to find generic  - Continuing",
                cu.getPrimaryTypeName().get(), coit.getName());
          } else if (!exception.getMessage().contains("ResolvedReferenceType")) {
            logger.error("Working on classes from {} - {}\n"
                    + " Caught exception :",
                cu.getPrimaryTypeName().get(), coit.getName(), exception);
            throw exception;
          }
        }
      }
      if (typeName != null) {
        usedTypes.add(typeName);
      }
      visited.add(coit);
    });
  }

  private void parsePackages(CompilationUnit cu) {
    packages.addAll(cu.findAll(PackageDeclaration.class)
                        .stream()
                        .map(PackageDeclaration::getNameAsString)
                        .collect(Collectors.toList()));
  }

  private void findMainMethods(CompilationUnit cu) {
    List<String> mains = cu.findAll(MethodDeclaration.class)
                             .stream()
                             .filter(MethodDeclaration::isStatic)
                             .filter(MethodDeclaration::isPublic)
                             .filter(m -> m.getType().isVoidType())
                             .filter(m -> m.getNameAsString().equals("main"))
                             .map(m -> ((ClassOrInterfaceDeclaration) m.getParentNode().get()).getNameAsString())
                             .collect(Collectors.toList());
    if (!mains.isEmpty()) {
      mainClasses.addAll(mains);
    }
  }

  private void parseFullyQualifiedClassNames(
      Path classPath, CompilationUnit cu, Set<String> assumedPackagePrefixesWithBuildFile) {
    Optional<PackageDeclaration> packageDeclaration = cu.getPackageDeclaration();
    String packageName = "";
    if (!packageDeclaration.isEmpty()) {
      packageName = packageDeclaration.get().getName().asString();
    }
    final String effectivePackageName = packageName;

    Path buildModulePath = workspace.relativize(classPath.getParent());

    boolean findBuildInParent = true;
    for (String packagePrefixWithBuildFile : assumedPackagePrefixesWithBuildFile) {
      if (buildModulePath.toString().startsWith(packagePrefixWithBuildFile)
          || packagePrefixWithBuildFile.equals("all")) {
        findBuildInParent = false;
        break;
      }
    }
    if (findBuildInParent) {
      Path parentModule = classPath.getParent();
      while (parentModule.getNameCount() >= workspace.getNameCount()) {
        Path buildFilePath = Paths.get(parentModule.toString() + "/BUILD.bazel");
        if (Files.exists(buildFilePath)) {
          buildModulePath = workspace.relativize(parentModule);
          break;
        }
        parentModule = parentModule.getParent();
      }
    }
    final Path effectiveBuildModulePath = buildModulePath;

    classMetadatas.addAll(
        cu.getTypes()
            .parallelStream()
            .map(type
                -> new ClassMetadata(effectiveBuildModulePath.toString(), effectivePackageName + "." + type.getName()))
            .collect(Collectors.toSet()));
  }
}
