/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import io.harness.bundler.SchemaBundler;
import io.harness.bundler.SchemaBundlerRegistrar;

import java.io.IOException;
import java.util.Map;

class SchemaBundleTool {
  public static void main(String[] args) throws IOException {
    SchemaBundlerRegistrar schemaBundlerRegistrar = new SchemaBundlerRegistrar();

    for (Map.Entry<YamlEntityType, SchemaBundler> entry :
        schemaBundlerRegistrar.getRegisteredSchemaBundlers().entrySet()) {
      entry.getValue().bundle();
    }
  }
}
