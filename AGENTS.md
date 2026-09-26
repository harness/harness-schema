# AGENTS.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is the Harness NextGen Pipeline YAML Schema repository. It contains JSON Schema definitions for Harness pipeline configuration files, enabling YAML intellisense and validation in IDEs.

## Architecture

### Schema Definition Flow

1. **YAML Source Files** (`v0/pipeline/`, `v0/template/`, `v1/pipeline/`, `v1/template/`, `v1/agent/`) - Human-editable schema definitions
2. **JSON Output** (`v0/*.json`, `v1/*.json`) - Bundled schemas consumed by IDEs and services

v1 also has first-class documents besides pipeline and template: `v1/agent/` (custom agent YAML stored by ACM; bundled as `v1/agent.json`).

### Key Directories

- `v0/pipeline/steps/{category}/` - Step definitions organized by category (cd, ci, common, custom, cvng, iacm, idp)
- `v0/pipeline/stages/{stage}/` - Stage definitions (approval, cd, cf, ci, custom, iacm, idp, security)
- `v0/pipeline/common/` - Shared schema components
- `v0/template/` - Template-specific schema definitions
- `v1/agent/` - Custom agent document (`agent:` root; nested `uses: harnessAI@version`)

### Step Schema Structure

Each step requires two YAML files:
- `{step-name}-step-info.yaml` - Defines the step's spec properties
- `{step-name}-step-node.yaml` - Defines the step wrapper (identifier, name, timeout, type, etc.)

Steps are made available in stages by adding references to `v0/pipeline/stages/{stage}/execution-wrapper-config.yaml`.

### Template Support

Steps can be used as templates by adding them to `v0/template/template_config.yaml` under `step_template_types:`.

## Common Patterns

### Expression Support

Fields supporting Harness expressions (`<+...>`) and runtime input (`<+input>`) use:
```yaml
fieldName:
  oneOf:
    - $ref: ../common/string-without-jexl.yaml
    - $ref: ../common/common-jexl.yaml
```

### Step Type Enum

The `type` field in step nodes must match exactly (case-sensitive):
```yaml
type:
  type: string
  enum:
  - StepName
```
