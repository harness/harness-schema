/*
 * Copyright 2023 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import com.sun.jdi.request.InvalidRequestStateException;
import io.harness.bundler.SchemaBundler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import lombok.Builder;
import lombok.Getter;

@Builder
public class SchemaBundleUtils implements SchemaBundler {
  @Getter private YamlEntityType yamlEntityType;

  public final String DESCRIPTION = "description";
  public final String IDENTIFIER = "identifier";
  public final String NAME = "name";
  public final String ORG_IDENTIFIER = "orgIdentifier";
  public final String PROJECT_IDENTIFIER = "projectIdentifier";

  public final String TEMPLATE = "template";
  public final String TYPE = "type";
  private static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());
  private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
  private static final String REF_NODE = "$ref";
  private static final String CONFIG_REF_NODE = "configRef";
  private static final String TITLE = "title";
  private static final String DEFINITIONS = "definitions";

  public SchemaBundleUtils(YamlEntityType yamlEntityType) {
    this.yamlEntityType = yamlEntityType;
  }

  private Path getRootSchemaDirectoryPath() {
    return Paths.get(yamlEntityType.getEntityRootSchemaPathFromResource() + "/../").normalize();
  }

  @Override
  public void bundle() {
    try {
      String rootSchemaYaml = readFile(yamlEntityType.getEntityRootSchemaPathFromResource());
      JsonNode rootSchemaNode = convertYamlToJsonNode(YAML_OBJECT_MAPPER, rootSchemaYaml);
      ObjectNode definitionNode = YAML_OBJECT_MAPPER.createObjectNode();
      iterateJsonNode(definitionNode, rootSchemaNode, getRootSchemaDirectoryPath());

      ObjectNode rootSchemaObjectNode = (ObjectNode) rootSchemaNode;
      rootSchemaObjectNode.set(DEFINITIONS, definitionNode);
      String bundledSchema = JSON_OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(rootSchemaNode);
      String workingDir = System.getenv("BUILD_WORKSPACE_DIRECTORY");
      Files.write(Paths.get(workingDir +"/" + yamlEntityType.getSchemaVersion().getDirectoryPath() + yamlEntityType.getEntityName()
                      + ".json"),
          bundledSchema.getBytes(StandardCharsets.UTF_8));
    } catch (Exception ex) {
      throw new InvalidRequestStateException(String.format("Exception in doing I/O operations %s", ex));
    }
  }

  private void iterateJsonNode(ObjectNode definitionsNode, JsonNode jsonNode, Path parentDir) {
    if (jsonNode == null) {
      return;
    }

    if (jsonNode.isArray()) {
      final Iterator<JsonNode> elements = jsonNode.elements();
      while (elements.hasNext()) {
        iterateJsonNode(definitionsNode, elements.next(), parentDir);
      }
    }

    if (jsonNode.isObject()) {
      ObjectNode node = (ObjectNode) jsonNode;
      handleConfigRef(parentDir, node);
      handleRefs(definitionsNode, parentDir, node);
      final Iterator<JsonNode> elements = node.elements();
      while (elements.hasNext()) {
        iterateJsonNode(definitionsNode, elements.next(), parentDir);
      }
    }
  }

  private void handleRefs(ObjectNode definitionsNode, Path parentDir, ObjectNode node) {
    JsonNode refNode = node.remove(REF_NODE);
    if (refNode != null && refNode.isTextual()) {
      String refValue = refNode.textValue();
      if (refValue.endsWith(".yaml")) {
        // this adds refValue to parentDir path
        Path newPath = parentDir.resolve(refValue);
        // this normalises path resolving relative paths like ./ or ../
        newPath = newPath.normalize();
        String childYamlSchema = readFile(newPath.toString());
        JsonNode childSchemaNode = convertYamlToJsonNode(YAML_OBJECT_MAPPER, childYamlSchema);
        if (!childSchemaNode.has(TITLE)) {
          throw new InvalidRequestStateException("Title is missing from file: " + refValue);
        }

        if (refValue.contains("template_config")) {
          updateJsonForTemplate(childSchemaNode, refValue);
        }

        String childSchemaTitle = childSchemaNode.get(TITLE).asText();
        // We want to retain the folder structure in definitions
        // So, let's get the relative folder first from version directory.
        String relativePath = newPath.toString().replace(yamlEntityType.getSchemaVersion().getDirectoryPath(), "");
        int lastSlashIndex = relativePath.lastIndexOf("/");
        String directoryPathRelativeToRoot = "";
        if (lastSlashIndex != -1) {
          directoryPathRelativeToRoot = relativePath.substring(0, lastSlashIndex + 1);
        }
        // setting the definition with the folder structure maintained. Next is adding such node in definitions.
        String finalDefinitionRoute = directoryPathRelativeToRoot + childSchemaTitle;
        node.put(REF_NODE, "#/definitions/" + finalDefinitionRoute);
        if (!checkIfSuchDefinitionAlreadyExist(definitionsNode, finalDefinitionRoute)) {
          setNodeInDefinitions(definitionsNode, childSchemaNode, finalDefinitionRoute);
          iterateJsonNode(definitionsNode, childSchemaNode, Paths.get(newPath + "/../").normalize());
        }
      }
    }
  }

  private void updateJsonForTemplate(JsonNode childSchemaNode, String refValue) {
    String title = childSchemaNode.get(TITLE).asText();
    String newTitle = title + "_template";
    HashSet<String> keys = getKeysToRemoveFromTemplateSpec(refValue);
    if (refValue.contains("../pipeline/")) {
      deletePropertiesInJsonNode((ObjectNode) childSchemaNode.get("properties"), keys);
      deletePropertiesInArrayNode((ArrayNode) childSchemaNode.get("required"), keys);
      Map<String, String> propertiesToUpdate = new HashMap<>();
      propertiesToUpdate.put(TITLE, newTitle);
      updatePropertiesInJsonNode((ObjectNode) childSchemaNode, propertiesToUpdate);
    }
  }

  private  HashSet<String> getKeysToRemoveFromTemplateSpec(String refValue) {

    if (refValue.contains("step-group-element-config.yaml")) {
      return new HashSet<>(Arrays.asList(NAME, IDENTIFIER, DESCRIPTION, ORG_IDENTIFIER, PROJECT_IDENTIFIER, TYPE));
    } else if (refValue.contains("stage-node.yaml") || refValue.contains("step-node.yaml") ||refValue.contains("artifact-source.yaml")) {
      return new HashSet<>(Arrays.asList(NAME, IDENTIFIER, DESCRIPTION, ORG_IDENTIFIER, PROJECT_IDENTIFIER, TEMPLATE));
    }

    return new HashSet<>();
  }

  public static JsonNode deletePropertiesInJsonNode(ObjectNode jsonNode, Collection<String> properties) {
    if (EmptyPredicate.isEmpty(properties) || jsonNode == null) {
      return jsonNode;
    }
    for (String property : properties) {
      if (jsonNode.has(property)) {
        jsonNode.remove(property);
      }
    }
    return jsonNode;
  }
  public static ArrayNode deletePropertiesInArrayNode(ArrayNode arrayNode, Collection<String> keys) {
    if (EmptyPredicate.isEmpty(keys) || arrayNode == null) {
      return arrayNode;
    }
    int size = arrayNode.size();
    int j = 0;
    for (int i = 0; i < size; i++) {
      JsonNode node = arrayNode.get(j);
      if (node != null) {
        if (node.isTextual()) {
          if (keys.contains(node.asText())) {
            arrayNode.remove(j);
          } else {
            j++;
          }
        }
      }
    }

    return arrayNode;
  }

  public static JsonNode updatePropertiesInJsonNode(ObjectNode jsonNode, Map<String, String> properties) {
    if (EmptyPredicate.isEmpty(properties) || jsonNode == null) {
      return jsonNode;
    }
    for (Map.Entry<String, String> property : properties.entrySet()) {
      if (jsonNode.has(property.getKey())) {
        jsonNode.put(property.getKey(), property.getValue());
      }
    }
    return jsonNode;
  }
  private void handleConfigRef(Path parentDir, ObjectNode node) {
    JsonNode configRefNode = node.remove(CONFIG_REF_NODE);
    if (configRefNode != null && configRefNode.isTextual()) {
      String configRefValue = configRefNode.textValue();
      if (configRefValue.contains(".yaml")) {
        int lastSlashIndex = configRefValue.lastIndexOf("/");
        if (lastSlashIndex == -1) {
          throw new InvalidRequestStateException(String.format("ConfigRef %s does not have / in it.", configRefValue));
        }
        String configFileName = configRefValue.substring(0, lastSlashIndex);
        String fieldInConfigFile = configRefValue.substring(lastSlashIndex + 1);
        Path configFilePath = parentDir.resolve(configFileName);
        configFilePath = configFilePath.normalize();
        String configFile = readFile(configFilePath.toString());
        JsonNode configFileNode = convertYamlToJsonNode(YAML_OBJECT_MAPPER, configFile);
        if (!configFileNode.has(fieldInConfigFile)) {
          throw new InvalidRequestStateException("Invalid config file reference: " + configRefValue);
        }
        JsonNode configNode = configFileNode.get(fieldInConfigFile);
        if (configNode.isArray()) {
          ArrayNode oneOfNodes = YAML_OBJECT_MAPPER.createArrayNode();
          Iterator<JsonNode> elements = configNode.elements();
          while (elements.hasNext()) {
            ObjectNode n = YAML_OBJECT_MAPPER.createObjectNode();
            String value = elements.next().textValue();
            String configFileDirectoryRelativeToParent = configFileName + "/../";
            String refValueRelativeToParent = configFileDirectoryRelativeToParent + value;
            n.put(REF_NODE, refValueRelativeToParent);
            oneOfNodes.add(n);
          }
          node.set("oneOf", oneOfNodes);
        }
      }
    }
  }

  private boolean checkIfSuchDefinitionAlreadyExist(ObjectNode definitionNode, String definitionRoute) {
    ObjectNode traversalNode = definitionNode;
    String[] directorySplits = definitionRoute.split("/");
    for (String dirSplit : directorySplits) {
      if (traversalNode.get(dirSplit) != null) {
        JsonNode tempNode = traversalNode.get(dirSplit);
        if (tempNode.isObject()) {
          traversalNode = (ObjectNode) tempNode;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  private void setNodeInDefinitions(ObjectNode definitionsNode, JsonNode childSchemaNode, String definitionRoute) {
    ObjectNode traversalNode = definitionsNode;
    String[] directorySplits = definitionRoute.split("/");
    for (int i = 0; i < directorySplits.length - 1; i++) {
      String dirSplit = directorySplits[i];
      if (traversalNode.get(dirSplit) != null) {
        JsonNode tempNode = traversalNode.get(dirSplit);
        if (!tempNode.isObject()) {
          traversalNode.set(dirSplit, YAML_OBJECT_MAPPER.createObjectNode());
        }
      } else {
        traversalNode.set(dirSplit, YAML_OBJECT_MAPPER.createObjectNode());
      }
      traversalNode = (ObjectNode) traversalNode.get(dirSplit);
    }
    String leafNode = directorySplits[directorySplits.length - 1];
    traversalNode.set(leafNode, childSchemaNode);
  }

  private JsonNode convertYamlToJsonNode(ObjectMapper objectMapper, String yaml) {
    if (EmptyPredicate.isEmpty(yaml)) {
      throw new InvalidRequestStateException("Yaml being converted to jsonNode is empty");
    }
    try {
      return objectMapper.readValue(yaml, JsonNode.class);
    } catch (JsonProcessingException e) {
      throw new InvalidRequestStateException(e.getMessage());
    }
  }

  /**
   * The method returns file given from resource folder.
   * @param filename It should be fileName inside resources' folder. In case file is present in subdirectory,
   *                 fileName should be directory name / fileName.
   * @return file content
   */
  private String readFile(String filename) {
    ClassLoader classLoader = getClass().getClassLoader();
    try {
      return Resources.toString(Objects.requireNonNull(classLoader.getResource(filename)), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new InvalidRequestStateException("Could not read resource file: " + filename);
    }
  }
}
