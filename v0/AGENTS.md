# v0/ Schema Directory

This directory contains the source YAML schemas and bundled JSON output for Harness pipeline configuration.

## Directory Structure

```
v0/
├── pipeline/
│   ├── steps/          # Step definitions by category
│   │   ├── cd/         # CD-specific steps (K8s, Helm, ECS, etc.)
│   │   ├── ci/         # CI-specific steps (build, cache, etc.)
│   │   ├── common/     # Shared field definitions and reusable components
│   │   ├── custom/     # Custom steps (FME flags, HTTP, approvals, etc.)
│   │   ├── cvng/       # Continuous Verification steps
│   │   ├── iacm/       # Infrastructure as Code steps
│   │   └── idp/        # Internal Developer Portal steps
│   ├── stages/         # Stage definitions
│   │   ├── cd/         # Deployment stage
│   │   ├── ci/         # Build stage
│   │   ├── cf/         # Feature Flag stage
│   │   ├── custom/     # Custom stage
│   │   ├── approval/   # Approval stage
│   │   ├── security/   # Security stage
│   │   ├── iacm/       # IACM stage
│   │   └── idp/        # IDP stage
│   ├── common/         # Shared pipeline components
│   └── schema_config.yaml  # Stage registration
├── template/
│   └── template_config.yaml  # Template type registration
├── trigger/            # Trigger schema definitions
├── pipeline.json       # Bundled pipeline schema (generated)
├── template.json       # Bundled template schema (generated)
└── trigger.json        # Bundled trigger schema (generated)
```

## Common Schema Files

### Expression and Input Patterns

Located in `v0/pipeline/steps/common/`:

| File | Description | Use Case |
|------|-------------|----------|
| `common-jexl.yaml` | Matches JEXL expressions (`<+...>`) including runtime input (`<+input>`) | Fields supporting expressions and runtime input |
| `string-without-jexl.yaml` | Plain string excluding JEXL expressions | Use with `common-jexl.yaml` in `oneOf` for mutual exclusivity |
| `common-jexl-expression-only.yaml` | JEXL expressions excluding `<+input>` | Fields supporting expressions but NOT runtime input |

### Reusable Field Patterns

Located in `v0/pipeline/steps/common/`:

| File | Description |
|------|-------------|
| `fme-flag-common-flag-name.yaml` | Feature flag name validation (alphanumeric, hyphens, underscores) |
| `fme-flag-common-environment.yaml` | FME environment reference |
| `fme-flag-common-treatment.yaml` | FME treatment/variation reference |

### Base Types

Located in `v0/pipeline/common/`:

| File | Description |
|------|-------------|
| `step-spec-type.yaml` | Base type for all step info schemas (extend with `allOf`) |
| `step-when-condition.yaml` | Conditional execution configuration |
| `failure-strategy-config.yaml` | Failure handling configuration |
| `strategy-config.yaml` | Looping/matrix strategy configuration |
| `policy-config.yaml` | OPA policy enforcement configuration |

## Common Field Patterns

### String with expression and runtime input support
```yaml
fieldName:
  description: "Field description"
  oneOf:
    - $ref: ../common/string-without-jexl.yaml
    - $ref: ../common/common-jexl.yaml
```

### String with expression support only (no runtime input)
```yaml
fieldName:
  oneOf:
    - $ref: ../common/string-without-jexl.yaml
    - $ref: ../common/common-jexl-expression-only.yaml
```

### Boolean with expression support
```yaml
fieldName:
  oneOf:
    - type: boolean
    - $ref: ../common/common-jexl.yaml
```

### Array with expression support
```yaml
fieldName:
  oneOf:
    - type: array
      items:
        type: string
    - $ref: ../common/common-jexl.yaml
```

### Enum field (fixed values only)
```yaml
fieldName:
  type: string
  enum:
    - value1
    - value2
```

### Enum with expression support
```yaml
fieldName:
  oneOf:
    - type: string
      enum:
        - value1
        - value2
    - $ref: ../common/common-jexl.yaml
```

## Creating a New Step

### 1. Create Step Info YAML

`v0/pipeline/steps/{category}/{step-name}-step-info.yaml`:
```yaml
title: {StepName}StepInfo
allOf:
  - $ref: ../../common/step-spec-type.yaml
  - type: object
    required:
    - requiredField
    properties:
      fieldName:
        description: "Field description"
        oneOf:
          - $ref: ../common/string-without-jexl.yaml
          - $ref: ../common/common-jexl.yaml
$schema: http://json-schema.org/draft-07/schema#
```

### 2. Create Step Node YAML

`v0/pipeline/steps/{category}/{step-name}-step-node.yaml` - see `.claude/skills/harness-step-schema/TEMPLATES.md` for full template.

Key properties: `identifier`, `name`, `type`, `spec`, `timeout`, `failureStrategies`, `when`, `strategy`.

### 3. Register in Stages

Add to each target stage's `execution-wrapper-config.yaml`:
```yaml
    - $ref: ../../steps/{category}/{step-name}-step-node.yaml
```

### 4. Enable as Template (optional)

Add to `v0/template/template_config.yaml` under `step_template_types:`:
```yaml
  - ../pipeline/steps/{category}/{step-name}-step-node.yaml
```

### 5. Update Bundled JSON

Add definitions to both `pipeline.json` and `template.json`:
- `{StepName}StepNode` definition
- `{StepName}StepInfo` definition
- References in stage ExecutionWrapperConfig oneOf arrays

For `template.json`, also add `{StepName}StepNode_template` (without identifier/name required).

## Stage-Step Availability

Steps can be in multiple stages. Common patterns:
- **FME Flag steps**: cd, cf, custom
- **Approval steps**: approval, cd, custom
- **Shell Script, HTTP**: cd, cf, custom
- **Build steps**: ci
- **Security scans**: ci, security
- **Terraform/Terragrunt**: cd, cf, custom
