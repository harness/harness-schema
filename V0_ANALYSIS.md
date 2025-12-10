# V0 Schema System - Analysis & Implementation Guide

## Overview
The V0 directory implements a **JSON Schema bundling system** for Harness pipeline YAML validation. It converts modular YAML schema definitions into a single consolidated JSON schema file that IDEs can use for auto-completion and validation.

---

## 📁 Directory Structure

### V0 Layout
```
v0/
├── BUILD.bazel                    # Bazel build configuration
├── pipeline/                      # Pipeline schema definitions
│   ├── pipeline_root.yaml        # Entry point for pipeline schema
│   ├── pipeline.yaml             # Main pipeline schema
│   ├── build.yaml                # Build type definitions (branch, tag, PR, commitSha)
│   ├── codebase.yaml             # Codebase configuration
│   ├── common/                   # Common schema components (50 items)
│   ├── stages/                   # Stage definitions (245 items)
│   └── steps/                    # Step definitions (999 items)
├── pipeline.json                 # GENERATED: Bundled pipeline schema (2.8MB)
├── template/                     # Template schema definitions
│   ├── template_root.yaml        # Entry point for template schema
│   └── ...
├── template.json                 # GENERATED: Bundled template schema (3.5MB)
├── trigger/                      # Trigger schema definitions (79 items)
└── trigger.json                  # GENERATED: Bundled trigger schema (101KB)
```

### Key Files
- **`pipeline_root.yaml`**: Top-level entry point that references `pipeline.yaml`
- **`build.yaml`**: Contains conditional schemas for different build types (branch, tag, PR, commitSha)
- **Schema files (*.yaml)**: Modular YAML definitions using JSON Schema format
- **JSON files (*.json)**: Auto-generated bundled schemas (DO NOT EDIT MANUALLY)

---

## 🔧 How It Works

### 1. Schema Definition System

#### Modular YAML Schemas
Each YAML file contains a JSON Schema definition:

```yaml
# Example: build.yaml
title: build
type: object
required:
  - spec
  - type
properties:
  type:
    type: string
    enum:
      - branch
      - tag
      - PR
      - commitSha
"$schema": http://json-schema.org/draft-07/schema#
allOf:
  - if:
      properties:
        type:
          const: commitSha
    then:
      properties:
        spec:
          type: object
          required:
            - commitSha
          properties:
            commitSha:
              type: string
```

#### Reference System
Schemas reference each other using `$ref`:
```yaml
# pipeline_root.yaml
properties:
  pipeline:
    $ref: "./pipeline.yaml"
```

#### Config References
For complex polymorphic types, use `configRef`:
```yaml
# References a config file containing multiple options
configRef: "./stages/config.yaml/stageTypes"
```

---

### 2. Bundling Process

#### Command
```bash
bazel run bundler/schema_store:module
```

#### What Happens

**Step 1: Entry Point Discovery**
- Reads `YamlEntityType` enum from Java code
- For V0 PIPELINE: starts at `v0/pipeline/pipeline_root.yaml`

**Step 2: Schema Traversal** (`SchemaBundleUtils.java`)
```
1. Parse root YAML → Convert to JSON Node
2. Find all $ref references to .yaml files
3. Read referenced file, extract title
4. Add to definitions with folder structure: #/definitions/path/title
5. Replace $ref with internal reference
6. Recursively process child schemas
7. Handle configRef for polymorphic types
```

**Step 3: Definition Building**
```json
{
  "definitions": {
    "pipeline": { ... },
    "stages": {
      "ci-stage": { ... },
      "cd-stage": { ... }
    },
    "steps": {
      "ci": {
        "run-step": { ... }
      }
    }
  }
}
```

**Step 4: Output Generation**
- Writes to `v0/pipeline.json` (2.8MB)
- Writes to `v0/template.json` (3.5MB)
- Writes to `v0/trigger.json` (101KB)

---

### 3. Key Java Components

#### `YamlEntityType.java`
Defines entities to bundle:
```java
public enum YamlEntityType {
  PIPELINE_V0("pipeline", SchemaVersion.V0, "pipeline/pipeline_root.yaml"),
  TEMPLATE_V0("template", SchemaVersion.V0, "template/template_root.yaml"),
  TRIGGER_V0("trigger", SchemaVersion.V0, "trigger/trigger_root.yaml"),
  // ...
}
```

#### `SchemaBundlerRegistrar.java`
Registers bundlers for each entity type:
```java
registeredSchemaBundlers.put(
  YamlEntityType.PIPELINE_V0, 
  SchemaBundleUtils.builder().yamlEntityType(YamlEntityType.PIPELINE_V0).build()
);
```

#### `SchemaBundleUtils.java` (334 lines)
Core bundling logic:
- `bundle()` - Main entry point
- `iterateJsonNode()` - Recursive traversal
- `handleRefs()` - Process $ref references
- `handleConfigRef()` - Handle configRef for polymorphic types
- `setNodeInDefinitions()` - Add to definitions with folder structure

---

## 🔍 Key Differences: V0 vs V1

### Entry Points
- **V0**: `pipeline_root.yaml` → `pipeline.yaml`
- **V1**: `pipeline_root.yaml` → `pipeline.yaml` (different structure)

### Pipeline Schema Structure

#### V0 Structure
```yaml
# v0/pipeline/pipeline.yaml - 65 lines
type: object
title: pipeline
properties:
  identifier: ...
  name: ...
  stages: ...
  properties:
    ci:
      codebase: ...
```

#### V1 Structure  
```yaml
# v1/pipeline/pipeline.yaml - 39 lines
type: object
title: pipeline
required:
  - spec
  - version
  - kind
properties:
  version:
    type: number
    enum: [1]
  kind:
    enum: [pipeline]
  spec:
    $ref: ./pipeline_spec.yaml
```

### Build Type Support
- **V0**: Supports `branch`, `tag`, `PR`, `commitSha` (4 types)
- **V1**: Supports `branch`, `tag`, `PR` (3 types) - **commitSha REMOVED**

### File Counts
- **V0 Pipeline**: 1316 items
- **V1 Pipeline**: 1171 items (145 fewer files)

### New V1 Features
- Added `version` and `kind` fields (API-style versioning)
- Separated `pipeline_spec.yaml` for cleaner structure
- New files: `clone.yaml`, `registry.yaml`, `options.yaml`, `reference.yaml`
- New entities: `inputSet`, `overlayInputSet`, `service`, `infra`

---

## 🎯 How to Achieve Similar Thing in V1

### Step 1: Understand Current V1 State
```bash
# V1 already has the bundling configured!
# Check YamlEntityType.java lines 17-24
```

V1 entities already registered:
- ✅ PIPELINE_V1
- ✅ TEMPLATE_V1
- ✅ INPUT_SET_V1
- ✅ OVERLAY_INPUT_SET_V1
- ✅ TRIGGER_V1
- ✅ SERVICE_V1
- ✅ INFRA_V1

### Step 2: Adding commitSha Support to V1

#### Option A: Restore in build.yaml
```yaml
# Edit: v1/pipeline/build.yaml
properties:
  type:
    type: string
    enum:
      - branch
      - tag
      - PR
      - commitSha  # ADD THIS

# Add allOf condition:
allOf:
  # ... existing conditions ...
  - if:
      properties:
        type:
          const: commitSha
    then:
      properties:
        spec:
          type: object
          required:
            - commitSha
          properties:
            commitSha:
              type: string
```

#### Option B: Create New Schema File
```bash
# If you need complex commitSha support
touch v1/pipeline/steps/ci/commit-sha-build-spec.yaml
# Copy from v0 and adapt
```

### Step 3: Run Bundler
```bash
bazel run bundler/schema_store:module
```

This will:
1. Read all V1 schemas
2. Bundle them into `v1/pipeline.json`, `v1/template.json`, etc.
3. Update `v1/service.json` and `v1/infra.json` (new in V1)

### Step 4: Test in IDE
```json
// VS Code settings.json
{
  "yaml.schemas": {
    "https://raw.githubusercontent.com/harness/harness-schema/main/v1/pipeline.json": "pipeline.yaml"
  }
}
```

---

## 📋 Best Practices

### 1. Schema File Conventions
- **Title field is MANDATORY**: Used for definition names
- **Use relative paths**: `$ref: "./common/string-variable.yaml"`
- **Maintain folder structure**: Preserved in bundled definitions
- **Add descriptions**: Helps IDE autocomplete

### 2. Adding New Step Types
```yaml
# 1. Create file: v1/pipeline/steps/custom/my-step.yaml
title: MyCustomStep
type: object
properties:
  type:
    const: MyStep
  spec:
    $ref: ./my-step-spec.yaml

# 2. Add to parent config (if needed)
# 3. Run bundler
# 4. Commit both .yaml and generated .json
```

### 3. Handling Breaking Changes
- V0 and V1 coexist independently
- Users specify version in YAML (`version: 1`)
- Old pipelines continue using V0 schema
- New pipelines use V1 schema

---

## 🚀 Common Tasks

### Add New Field to Pipeline
1. Edit `v1/pipeline/pipeline_spec.yaml`
2. Add property with JSON Schema definition
3. Run: `bazel run bundler/schema_store:module`
4. Test in IDE

### Add New Stage Type
1. Create `v1/pipeline/stages/my-stage.yaml`
2. Add to `stages/stages.yaml` config
3. Run bundler
4. Add examples in `v1/examples/pipeline/`

### Debug Schema Issues
```bash
# Validate YAML syntax
yamllint v1/pipeline/build.yaml

# Check generated JSON
cat v1/pipeline.json | jq '.definitions.pipeline'

# Test with real pipeline YAML
# Use online JSON schema validator
```

---

## 📊 Statistics

| Metric | V0 | V1 |
|--------|----|----|
| Pipeline Items | 1,316 | 1,171 |
| Pipeline JSON Size | 2.8MB | 2.2MB |
| Template Items | 18 | 15 |
| Template JSON Size | 3.5MB | 2.8MB |
| Trigger Items | 79 | 77 |
| Build Types | 4 (includes commitSha) | 3 (no commitSha) |
| New Entities | - | inputSet, overlayInputSet, service, infra |

---

## 🔗 Integration Points

### 1. Pipeline Service
- Fetches schema from `v0/pipeline.json` or `v1/pipeline.json`
- Flag: `USE_SCHEMA_FROM_HARNESS_SCHEMA_REPO`
- Default: False (uses local copy)
- DevSpace: True (fetches from repo)

### 2. IDE Integration
- VS Code: `yaml.schemas` setting
- IntelliJ: External schemas configuration
- Sublime: YAML package settings

### 3. CI/CD
- Schema changes trigger rebuild
- Generated files committed to repo
- Branch cut merges latest schemas

---

## ✅ Summary

**What V0 Does:**
- Provides modular YAML schema definitions
- Bundles them into single JSON schema files
- Enables IDE autocomplete for pipeline YAML
- Supports 4 build types including commitSha

**What You Need for V1:**
1. V1 already has the bundling system configured
2. To add commitSha: Edit `v1/pipeline/build.yaml`
3. Run `bazel run bundler/schema_store:module`
4. Test with IDE integration

**Key Takeaway:**
The infrastructure is identical between V0 and V1. You just need to modify the schema files and run the bundler!
