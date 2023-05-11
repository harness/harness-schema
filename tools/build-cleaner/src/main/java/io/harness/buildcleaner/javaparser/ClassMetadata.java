/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.javaparser;

public class ClassMetadata {
  private final String buildModulePath;
  private final String fullyQualifiedClassName;

  public ClassMetadata(String buildModulePath, String fullyQualifiedClassName) {
    this.buildModulePath = buildModulePath;
    this.fullyQualifiedClassName = fullyQualifiedClassName;
  }

  public String getBuildModulePath() {
    return buildModulePath;
  }

  public String getFullyQualifiedClassName() {
    return fullyQualifiedClassName;
  }
}
