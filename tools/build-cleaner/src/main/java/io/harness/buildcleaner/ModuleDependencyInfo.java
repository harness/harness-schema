/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModuleDependencyInfo {
  public String parentModule;
  public String subModule;
  public List<String> intraDependencies;
  public List<String> interDependencies;

  ModuleDependencyInfo(String module, String path) {
    parentModule = module.substring(0, module.indexOf("/"));
    subModule = path;
    interDependencies = new ArrayList<>();
    intraDependencies = new ArrayList<>();
  }

  /**
   * Method to add a dependency to this sub-module.
   *
   * @param dependency the string path for the dependency
   */
  public void addDependency(String dependency) {
    int slashIndex = dependency.substring(2).indexOf("/");
    if (slashIndex == -1) {
      return;
    }

    String parsedDep = dependency.substring(2, slashIndex + 2);

    if (parsedDep.equals(parentModule)) {
      intraDependencies.add(dependency);
    } else {
      interDependencies.add(dependency);
    }
  }

  /**
   * Method to compute this sub-module complexity based on
   * its intra module dependencies.
   *
   * 1. The intra module dependencies are determined as below -
   *    - For 970-ng-commons module all the dependencies within 970-ng-commons is considered intra.
   *    - And other dependencies on 980-commons, 970-api-service etc are considered inter.
   *
   * 2. Compare the path of the dependency with the subModule and determine if it is in the same path
   *    or different path. This is determined as below -
   *
   *    Ex 1: subModule = {970-grpc/src/main/java/io/harness/grpc/auth}
   *        dep1 = {970-grpc/src/main/java/io/harness/grpc/utils}
   *        dep2 = {970-grpc/src/main/java/io/harness/grpc}
   *
   * In this ex, the subModule has an overlapping path with its deps till {970-grpc/src/main/java/io/harness/grpc}.
   * However, after the overlap till depth of 7 there is a divergence. The subModule is of depth 8. Since the
   * overlapDepth {7} < subModuleDepth {8} both the deps are in a different path which increments the diffPathDeps.
   *
   *    Ex 2: subModule = {970-ng-commons/src/main/java/io/harness}
   *        dep1 = {970-ng-commons/src/main/java/io/harness/beans }
   *        dep2 = {970-ng-commons/src/main/java/io/harness/common}
   *
   * In this ex, the subModule has an overlapping path with its deps till {970-ng-commons/src/main/java/io/harness}.
   * Here, there has been no divergence in paths i.e both the deps are children packages of the subModule. Since the
   * overlapDepth {6} = subModuleDepth {6} both the deps are in the same path which increments the samePathDeps.
   *
   *  3. As per our observations in fixing cycles in other sub-modules one key thing that was noticed is
   *     that modules with different path dependencies had more cycles to fix than modules with same path
   *     dependencies i.e Ex-1 above has a higher probability of having more cycles than Ex-2.
   *
   *     Thus, for the final complexity computation a higher weight of 0.7 is assigned to diffPathDeps and a lower
   *     weight of 0.3 is assigned to samePathDeps.
   *
   *
   * @param modDepth the depth of this sub-module
   * @return
   */
  public double computeIntraModuleComplexity(int modDepth) {
    int samePathDeps = 0;
    int diffPathDeps = 0;

    log.info(" computeIntraModuleComplexity : for subModule { " + subModule + "}");
    for (String intraDep : intraDependencies) {
      String parsedDep = intraDep.substring(2, intraDep.indexOf(":"));
      int level = comparePaths(parsedDep + "/", subModule + "/");
      if (level < modDepth) {
        diffPathDeps++;
      } else {
        samePathDeps++;
      }
      log.info("computeIntraModuleComplexity : intraDep {" + parsedDep + "}, level {" + level + "}, moduleLevel {"
          + modDepth + "}, diffPathDeps {" + diffPathDeps + "}, samePathDeps {" + samePathDeps + "}");
    }

    return (diffPathDeps * 0.7) + (samePathDeps * 0.3);
  }

  /**
   * Helper method to compare the paths of 2 modules.
   * This method determines the length of the sub-path that
   * matches with each other for the given 2 modules.
   *
   * Ex 1: mod1 = 970-grpc/src/main/java/io/harness/grpc/auth
   *       mod2 = 970-grpc/src/main/java/io/harness/grpc/utils
   *
   *  For the above 2 modules until the sub-path {970-grpc/src/main/java/io/harness/grpc/}
   *  matches for both of them and its of depth 7. Return this sub-path match depth value.
   *
   *  Ex 2: mod1 = 970-ng-commons/src/main/java/io/harness
   *        mod2 = 970-ng-commons/src/main/java/io/harness/beans
   *
   *  For the above 2 modules until the sub-path {970-ng-commons/src/main/java/io/harness/}
   *  matches for both of them and its of depth 6. Return this sub-path match depth value.
   *
   * @param mod1 path for module 1
   * @param mod2 path for module 2
   * @return
   */
  private int comparePaths(String mod1, String mod2) {
    if (mod1.isEmpty() || mod2.isEmpty()) {
      return 0;
    }

    int len1 = mod1.indexOf("/") + 1;
    int len2 = mod2.indexOf("/") + 1;

    if (len1 == 0 || len2 == 0) {
      return 0;
    }

    if (mod1.substring(0, len1).equals(mod2.substring(0, len2))) {
      return comparePaths(mod1.substring(len1), mod2.substring(len2)) + 1;
    }

    return comparePaths(mod1.substring(len1), mod2.substring(len2));
  }

  /**
   * Method to get the number of intra module dependencies
   * @return count of intra module deps
   */
  public int getIntraDependenciesCount() {
    return intraDependencies.size();
  }
}
