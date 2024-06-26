/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository,
 * also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import lombok.Getter;

public enum YamlEntityType {
  PIPELINE_V0("pipeline", SchemaVersion.V0, "pipeline/pipeline_root.yaml"),
  TEMPLATE_V0("template", SchemaVersion.V0, "template/template_root.yaml"),
  TRIGGER_V0("trigger", SchemaVersion.V0, "trigger/trigger_root.yaml"),
  PIPELINE_V1("pipeline", SchemaVersion.V1, "pipeline/pipeline.yaml"),
  TEMPLATE_V1("template", SchemaVersion.V1, "template/template_root.yaml"),
  INPUT_SET_V1("inputSet", SchemaVersion.V1, "inputSet/inputSet.yaml"),
  OVERLAY_INPUT_SET_V1("overlayInputSet", SchemaVersion.V1,
                       "overlayInputSet/overlayInputSet.yaml"),
  TRIGGER_V1("trigger", SchemaVersion.V1, "trigger/trigger_root.yaml"),
  SERVICE_V1("service", SchemaVersion.V1, "serviceEntity/service-entity.yaml"),
  INFRA_V1("infra", SchemaVersion.V1, "infraStructureEntity/infra-def-entity.yaml");

  @Getter private final String entityName;
  @Getter private final String entityRootSchemaPath;
  @Getter private final SchemaVersion schemaVersion;

  YamlEntityType(String entityName, SchemaVersion schemaVersion,
                 String entityRootSchemaPath) {
    this.entityName = entityName;
    this.schemaVersion = schemaVersion;
    this.entityRootSchemaPath = entityRootSchemaPath;
  }

  public String getEntityRootSchemaPathFromResource() {
    return this.schemaVersion.getDirectoryPath() + this.entityRootSchemaPath;
  }
}
