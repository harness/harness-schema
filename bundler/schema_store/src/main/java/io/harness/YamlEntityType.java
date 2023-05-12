/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import lombok.Getter;

public enum YamlEntityType {
  PIPELINE_V0("pipeline", SchemaVersion.V0, "pipeline/pipeline_root.yaml"),
  TEMPLATE_V0("template", SchemaVersion.V0, "template/template_root.yaml");

  @Getter private final String entityName;
  @Getter private final String entityRootSchemaPath;
  @Getter private final SchemaVersion schemaVersion;

  YamlEntityType(String entityName, SchemaVersion schemaVersion, String entityRootSchemaPath) {
    this.entityName = entityName;
    this.schemaVersion = schemaVersion;
    this.entityRootSchemaPath = entityRootSchemaPath;
  }

  public String getEntityRootSchemaPathFromResource() {
    return this.schemaVersion.getDirectoryPath() + this.entityRootSchemaPath;
  }
}
