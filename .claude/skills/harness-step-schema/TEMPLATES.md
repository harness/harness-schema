# Step Schema Templates

## Step Info YAML Template

File: `v0/pipeline/steps/{category}/{step-name}-step-info.yaml`

```yaml
title: {StepName}StepInfo
allOf:
  - $ref: ../../common/step-spec-type.yaml
  - type: object
    required:
    - {required_field}
    properties:
      {fieldName}:
        description: "{Field description}"
        oneOf:
          - $ref: ../common/string-without-jexl.yaml
          - $ref: ../common/common-jexl.yaml
$schema: http://json-schema.org/draft-07/schema#
```

## Step Node YAML Template

File: `v0/pipeline/steps/{category}/{step-name}-step-node.yaml`

```yaml
title: {StepName}StepNode
type: object
required:
- identifier
- name
- spec
- type
properties:
  description:
    type: string
    desc: This is the description for {StepName}StepNode
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
    - {StepName}
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
        const: {StepName}
  then:
    properties:
      spec:
        $ref: {step-name}-step-info.yaml
```

## Stage Execution Wrapper Reference

Add to `v0/pipeline/stages/{stage}/execution-wrapper-config.yaml`:

```yaml
    - $ref: ../../steps/{category}/{step-name}-step-node.yaml
```

## Template Config Reference

Add to `v0/template/template_config.yaml` under `step_template_types:`:

```yaml
  - ../pipeline/steps/{category}/{step-name}-step-node.yaml
```

## JSON Definition Templates

### StepNode Definition (pipeline.json)

```json
"{StepName}StepNode" : {
  "title" : "{StepName}StepNode",
  "type" : "object",
  "required" : [ "identifier", "name", "spec", "type" ],
  "properties" : {
    "description" : {
      "type" : "string",
      "desc" : "This is the description for {StepName}StepNode"
    },
    "enforce" : {
      "$ref" : "#/definitions/pipeline/common/PolicyConfig"
    },
    "failureStrategies" : {
      "oneOf" : [ {
        "type" : "array",
        "items" : {
          "$ref" : "#/definitions/pipeline/common/FailureStrategyConfig"
        }
      }, {
        "type" : "string",
        "pattern" : "^<\\+input>$",
        "minLength" : 1
      } ]
    },
    "identifier" : {
      "type" : "string",
      "pattern" : "^[a-zA-Z_][0-9a-zA-Z_]{0,127}$"
    },
    "name" : {
      "type" : "string",
      "pattern" : "^[a-zA-Z_0-9-.][-0-9a-zA-Z_\\s.]{0,127}$"
    },
    "strategy" : {
      "oneOf" : [ {
        "$ref" : "#/definitions/pipeline/common/StrategyConfig"
      }, {
        "type" : "string",
        "pattern" : "^<\\+input>$",
        "minLength" : 1
      } ]
    },
    "timeout" : {
      "type" : "string",
      "pattern" : "^(([1-9])+\\d+[s])|(((([1-9])+\\d*[mhwd])+([\\s]?\\d+[smhwd])*)|(.*<\\+.*>(?!.*\\.executionInput\\(\\)).*)|(^$))$"
    },
    "type" : {
      "type" : "string",
      "enum" : [ "{StepName}" ]
    },
    "when" : {
      "oneOf" : [ {
        "$ref" : "#/definitions/pipeline/common/StepWhenCondition"
      }, {
        "type" : "string",
        "pattern" : "^<\\+input>$",
        "minLength" : 1
      } ]
    }
  },
  "$schema" : "http://json-schema.org/draft-07/schema#",
  "allOf" : [ {
    "if" : {
      "properties" : {
        "type" : {
          "const" : "{StepName}"
        }
      }
    },
    "then" : {
      "properties" : {
        "spec" : {
          "$ref" : "#/definitions/pipeline/steps/{category}/{StepName}StepInfo"
        }
      }
    }
  } ]
}
```

### StepNode_template Definition (template.json)

Same as StepNode but:
- Title: `{StepName}StepNode_template`
- Required: `[ "spec", "type" ]` (no identifier/name)
- Remove identifier and name properties

### StepInfo Definition

```json
"{StepName}StepInfo" : {
  "title" : "{StepName}StepInfo",
  "allOf" : [ {
    "$ref" : "#/definitions/pipeline/common/StepSpecType"
  }, {
    "type" : "object",
    "required" : [ "{requiredField}" ],
    "properties" : {
      "{fieldName}" : {
        "oneOf" : [ {
          "$ref" : "#/definitions/pipeline/steps/common/fme-flag-common-flag-name"
        }, {
          "$ref" : "#/definitions/pipeline/steps/common/common-jexl"
        } ]
      }
    }
  } ],
  "$schema" : "http://json-schema.org/draft-07/schema#"
}
```

## Common Field Patterns

### String with expression support
```yaml
fieldName:
  description: "Description"
  oneOf:
    - $ref: ../common/string-without-jexl.yaml
    - $ref: ../common/common-jexl.yaml
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

### Enum field
```yaml
fieldName:
  type: string
  enum:
    - value1
    - value2
```

### Flag name field (reusable pattern)
```yaml
name:
  oneOf:
    - $ref: ../common/fme-flag-common-flag-name.yaml
    - $ref: ../common/common-jexl.yaml
```
