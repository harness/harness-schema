/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import lombok.Getter;

public enum SchemaVersion {
  V0("v0/"),
  V1("v1/");

  @Getter private final String directoryPath;

  SchemaVersion(String directoryPath) {
    this.directoryPath = directoryPath;
  }
}
