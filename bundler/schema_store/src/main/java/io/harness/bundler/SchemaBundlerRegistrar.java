/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.bundler;

import io.harness.SchemaBundleUtils;
import io.harness.YamlEntityType;

import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;

public class SchemaBundlerRegistrar {
  @Getter
  private final Map<YamlEntityType, SchemaBundler> registeredSchemaBundlers = new EnumMap<>(YamlEntityType.class);
  public SchemaBundlerRegistrar() {
    registeredSchemaBundlers.put(
        YamlEntityType.PIPELINE_V0, SchemaBundleUtils.builder().yamlEntityType(YamlEntityType.PIPELINE_V0).build());
    registeredSchemaBundlers.put(
        YamlEntityType.TEMPLATE_V0, SchemaBundleUtils.builder().yamlEntityType(YamlEntityType.TEMPLATE_V0).build());
  }
}
