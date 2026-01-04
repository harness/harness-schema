# Step Schema Examples

## Example 1: Simple Step (FmeFlagDelete)

A minimal step with just one required field.

### Step Info
`v0/pipeline/steps/custom/fme-flag-delete-step-info.yaml`
```yaml
title: FmeFlagDeleteStepInfo
allOf:
  - $ref: ../../common/step-spec-type.yaml
  - type: object
    required:
    - name
    properties:
      name:
          oneOf:
            - $ref: ../common/fme-flag-common-flag-name.yaml
            - $ref: ../common/common-jexl.yaml
$schema: http://json-schema.org/draft-07/schema#
```

### Step Node
`v0/pipeline/steps/custom/fme-flag-delete-step-node.yaml`
```yaml
title: FmeFlagDeleteStepNode
type: object
required:
- identifier
- name
- spec
- type
properties:
  description:
    type: string
    desc: This is the description for FmeFlagDeleteStepNode
  enforce:
    $ref: ../../common/policy-config.yaml
  failureStrategies:
    oneOf:
    - type: array
      items:
        $ref: ../../common/failure-strategy-config.yaml
    - type: string
      pattern: ^<\+input>$
      minLength: 1
  identifier:
    type: string
    pattern: ^[a-zA-Z_][0-9a-zA-Z_]{0,127}$
  name:
    type: string
    pattern: ^[a-zA-Z_0-9-.][-0-9a-zA-Z_\s.]{0,127}$
  strategy:
    oneOf:
    - $ref: ../../common/strategy-config.yaml
    - type: string
      pattern: ^<\+input>$
      minLength: 1
  timeout:
    type: string
    pattern: ^(([1-9])+\d+[s])|(((([1-9])+\d*[mhwd])+([\s]?\d+[smhwd])*)|(.*<\+.*>(?!.*\.executionInput\(\)).*)|(^$))$
  type:
    type: string
    enum:
    - FmeFlagDelete
  when:
    oneOf:
    - $ref: ../../common/step-when-condition.yaml
    - type: string
      pattern: ^<\+input>$
      minLength: 1
$schema: http://json-schema.org/draft-07/schema#
allOf:
- if:
    properties:
      type:
        const: FmeFlagDelete
  then:
    properties:
      spec:
        $ref: fme-flag-delete-step-info.yaml
```

### Stages
Available in: cd, cf, custom

---

## Example 2: Complex Step (FmeFlagCreate)

A step with multiple fields including required, optional, arrays, and booleans.

### Step Info
`v0/pipeline/steps/custom/fme-flag-create-step-info.yaml`
```yaml
title: FmeFlagCreateStepInfo
allOf:
  - $ref: ../../common/step-spec-type.yaml
  - type: object
    required:
    - trafficType
    - name
    properties:
      trafficType:
        description: "FME Traffic Type name (case-sensitive)"
        oneOf:
          - $ref: ../common/string-without-jexl.yaml
          - $ref: ../common/common-jexl.yaml
      name:
          oneOf:
            - $ref: ../common/fme-flag-common-flag-name.yaml
            - $ref: ../common/common-jexl.yaml
      description:
        description: "Feature flag description"
        oneOf:
          - $ref: ../common/string-without-jexl.yaml
          - $ref: ../common/common-jexl.yaml
      owners:
        description: "Feature flag owners"
        oneOf:
          - type: array
            items:
              type: string
          - $ref: ../common/common-jexl.yaml
      tags:
        description: "Feature flag tags"
        oneOf:
          - type: array
            items:
              type: string
              pattern: ^([a-zA-Z0-9-_]+)$
              maxItems: 20
          - $ref: ../common/common-jexl.yaml
      addDefaultRolloutPlans:
        oneOf:
          - type: boolean
          - $ref: ../common/common-jexl.yaml
        description: "Creates all feature flag definitions in default state (\"off\")"
$schema: http://json-schema.org/draft-07/schema#
```

### Stages
Available in: cd, cf, custom

---

## Stage-Step Mapping Reference

Steps can be available in multiple stages simultaneously. Here are common patterns:

| Step Type | Stages Available |
|-----------|------------------|
| FME Flag steps | cd, cf, custom |
| Approval steps (Jira, ServiceNow, Harness) | approval, cd, custom |
| Shell Script, HTTP, Email | cd, cf, custom |
| CI build steps (Build & Push, Cache) | ci |
| Security scan steps (Snyk, Sonar, etc.) | ci, security |
| Terraform/Terragrunt steps | cd, cf, custom |
| K8s deployment steps | cd |
| Common utility (Run, Plugin, Background) | cd, ci, custom, cf |
| DBOPS steps | approval, cd, custom |
| Event Listener | cd, cf, ci, custom |

**Key insight:** The same step YAML files are referenced from multiple stage execution-wrapper-config.yaml files. You don't create separate step files per stage.

---

## Finding Existing Steps

### By category
```bash
ls v0/pipeline/steps/custom/   # Custom steps
ls v0/pipeline/steps/cd/       # CD steps
ls v0/pipeline/steps/common/   # Common steps
```

### Search for step in stages
```bash
grep -r "step-name" v0/pipeline/stages/*/execution-wrapper-config.yaml
```

### Check JSON references
```bash
grep "StepName" v0/pipeline.json | head -20
```
