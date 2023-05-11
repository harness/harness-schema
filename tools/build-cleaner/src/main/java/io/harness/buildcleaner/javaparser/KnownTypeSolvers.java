/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.javaparser;

import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KnownTypeSolvers {
  private static final Logger logger = LoggerFactory.getLogger(KnownTypeSolvers.class);
  /**
   * This is a map of a Java Type solver, the class that will look up (resolve or solve) a given
   * java type, to the bazel dependency name where Bazel will find the required java type. The
   * dependency name will be of the form: - artifact("com.example.library:library") - For a
   * dependency external to the repo and found in the maven cache - //src/java/ - For a dependency
   * in the repo the root of the source tree for searching - null - For all dependencies in the
   * default java library
   *
   * <p>The complete map will contain a ReflectionTypeSolver (for the java library), one type solver
   * for each jar file in the maven cache, and one type solver for each source project directory in
   * your repo, each with the corresponding bazel dependency name.
   */
  private final Map<TypeSolver, String> knownSolvers = new HashMap<>();

  /**
   * A collection of the same solvers in the knownSolvers. Used by the JavaParser internally for
   * type resolution during parsing.
   */
  private final CombinedTypeSolver typeSolver;

  public KnownTypeSolvers() {
    typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
    knownSolvers.put(new ReflectionTypeSolver(), null);
  }

  public JavaSymbolSolver getTypeSolver() {
    return new JavaSymbolSolver(typeSolver);
  }
}
