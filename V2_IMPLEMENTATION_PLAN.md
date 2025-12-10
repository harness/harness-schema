# V2 Implementation Plan - Bradrydzewski Spec

## Objective
Implement bradrydzewski spec-based schema for Harness V1 pipelines to achieve:
- 70-90% YAML verbosity reduction
- GitHub Actions compatibility
- Modern, intuitive syntax
- Backward compatibility through conversion layer

---

## Decision: V2 vs V1 Replacement

### Recommendation: Create V2 Directory

**Reasons:**
1. Non-breaking - V0 and V1 continue to work
2. Gradual migration path
3. Side-by-side comparison
4. Easy rollback if needed
5. Can run both in production simultaneously

---

## Implementation Phases

### ✅ Phase 1: Analysis & Planning (COMPLETED)
- [x] Analyzed bradrydzewski/spec repository
- [x] Documented specification structure
- [x] Identified key differences from V1
- [x] Created migration examples
- [x] Defined implementation checklist

### 📋 Phase 2: Schema Creation (IN PROGRESS)

#### Step 2.1: Directory Structure
```
v2/
├── BUILD.bazel
├── pipeline/
│   ├── pipeline_root.yaml       # Entry point
│   ├── pipeline.yaml             # Main pipeline
│   ├── stage.yaml                # Stage definition
│   ├── step.yaml                 # Step definition
│   │
│   ├── steps/                    # Step types
│   │   ├── step_run.yaml
│   │   ├── step_action.yaml
│   │   ├── step_approval.yaml
│   │   ├── step_barrier.yaml
│   │   ├── step_clone.yaml
│   │   ├── step_group.yaml
│   │   ├── step_queue.yaml
│   │   ├── step_template.yaml
│   │   └── step_test.yaml
│   │
│   ├── common/                   # Common components
│   │   ├── container.yaml
│   │   ├── strategy.yaml
│   │   ├── failure.yaml
│   │   ├── input.yaml
│   │   ├── on.yaml
│   │   ├── cache.yaml
│   │   ├── clone.yaml
│   │   ├── concurrency.yaml
│   │   ├── environment.yaml
│   │   ├── service.yaml
│   │   ├── repository.yaml
│   │   ├── runtime.yaml
│   │   ├── platform.yaml
│   │   ├── volume.yaml
│   │   ├── workspace.yaml
│   │   ├── permissions.yaml
│   │   └── status.yaml
│   │
│   └── examples/                 # Example YAMLs
│       ├── simple.yaml
│       ├── matrix.yaml
│       ├── github-compat.yaml
│       └── advanced.yaml
│
├── template/                     # Templates (if needed)
└── pipeline.json                 # Generated bundle
```

#### Step 2.2: Core Component Priority
1. **pipeline.yaml** - Root structure
2. **stage.yaml** - Stage definition
3. **step.yaml** - Step wrapper with all types
4. **step_run.yaml** - Most common step type
5. **container.yaml** - Container configuration
6. **strategy.yaml** - Matrix/looping
7. **failure.yaml** - Error handling

### 📋 Phase 3: Java Bundler Updates

#### Update `YamlEntityType.java`
```java
public enum YamlEntityType {
  // ... existing ...
  PIPELINE_V2("pipeline", SchemaVersion.V2, "pipeline/pipeline_root.yaml"),
  TEMPLATE_V2("template", SchemaVersion.V2, "template/template_root.yaml"),
}
```

#### Update `SchemaVersion.java`
```java
public enum SchemaVersion {
  V0("v0/"),
  V1("v1/"),
  V2("v2/");  // ADD THIS
  
  @Getter private final String directoryPath;
  
  SchemaVersion(String directoryPath) {
    this.directoryPath = directoryPath;
  }
}
```

#### Update `SchemaBundlerRegistrar.java`
```java
registeredSchemaBundlers.put(
  YamlEntityType.PIPELINE_V2,
  SchemaBundleUtils.builder().yamlEntityType(YamlEntityType.PIPELINE_V2).build()
);
```

### 📋 Phase 4: Testing & Validation
- [ ] Create unit tests for schema validation
- [ ] Test with sample YAML files
- [ ] Verify IDE autocomplete (VS Code)
- [ ] Test bundler generation
- [ ] Validate against bradrydzewski samples

### 📋 Phase 5: Documentation
- [ ] Update README.md with V2 information
- [ ] Create migration guide (V1 → V2)
- [ ] Document new syntax features
- [ ] Provide examples for common use cases

### 📋 Phase 6: Integration
- [ ] Update pipeline service to support V2
- [ ] Add version detection (version: 2 or infer from structure)
- [ ] Create conversion utilities (V1 → V2)
- [ ] Update UI YAML editor

---

## Schema Design Principles

### 1. Simplicity First
```yaml
# Simple case should be simple
pipeline:
  stages:
  - steps:
    - go build

# Complex case should be possible
pipeline:
  env:
    GOOS: linux
  stages:
  - id: build
    strategy:
      matrix:
        version: ["1.19", "1.20"]
    steps:
    - run:
        script: go build
        container: golang:${{ matrix.version }}
```

### 2. Multi-Syntax Support
Every configurable element should support both short and long forms:

```yaml
# Container
container: golang                    # Short
container:                           # Long
  image: golang
  memory: 1gb

# Run step
- go build                           # Shortest
- run: go build                      # Short
- run:                               # Long
    script: go build
```

### 3. Smart Defaults
Minimize required fields:
```yaml
# Minimal valid pipeline
pipeline:
  stages:
  - steps:
    - go build
```

### 4. Type Safety
Use JSON Schema's advanced features:
- `oneOf` for mutually exclusive options
- `allOf` for composition
- `$ref` for reusability
- Conditional schemas with `if/then/else`

### 5. GitHub Compatibility
Support GitHub Actions syntax natively:
```yaml
# This should work!
jobs:
  test:
    runs-on: ubuntu
    steps:
      - run: go build
```

---

## Key Schema Patterns

### Pattern 1: Short/Long Forms
```yaml
# In YAML schema
oneOf:
  - type: string          # Short form
  - type: object          # Long form
    properties:
      # ... full config
```

### Pattern 2: Optional Step Type Wrapper
```yaml
# Both should work:
- run: go build           # Direct step type
- id: step1               # Wrapped with metadata
  run: go build
```

### Pattern 3: Flexible Arrays
```yaml
# Single or multiple
delegate: my-delegate
delegate:
  - delegate-1
  - delegate-2
```

---

## Sample Schema Snippets

### pipeline_root.yaml
```yaml
title: pipeline_root
type: object
required:
  - pipeline
properties:
  pipeline:
    $ref: "./pipeline.yaml"
"$schema": http://json-schema.org/draft-07/schema#
```

### pipeline.yaml (Core Structure)
```yaml
title: pipeline
type: object
properties:
  # Simple fields
  id:
    type: string
    description: "Unique pipeline identifier (deprecated)"
  name:
    type: string
    description: "Pipeline name (deprecated)"
  
  # Complex refs
  inputs:
    type: object
    additionalProperties:
      $ref: "./common/input.yaml"
  
  delegate:
    oneOf:
      - type: string
      - type: array
        items:
          type: string
    description: "Default delegate for all stages"
  
  env:
    type: object
    additionalProperties:
      type: string
    description: "Global environment variables"
  
  service:
    $ref: "./common/service.yaml"
  
  environment:
    $ref: "./common/environment.yaml"
  
  stages:
    type: array
    items:
      $ref: "./stage.yaml"
    minItems: 1
  
  repo:
    $ref: "./common/repository.yaml"
  
  clone:
    $ref: "./common/clone.yaml"
  
  if:
    type: string
    description: "Conditional pipeline execution"
  
  on:
    $ref: "./common/on.yaml"
  
  timeout:
    type: string
    pattern: "^([0-9]+[smhd])+$"
    description: "Pipeline timeout (e.g., 1h, 30m)"
  
  barriers:
    type: array
    items:
      type: string
  
  # GitHub compatibility
  jobs:
    type: object
    additionalProperties:
      $ref: "./stage.yaml"
    description: "GitHub Actions compatible jobs syntax"
  
  concurrency:
    $ref: "./common/concurrency.yaml"
  
  permissions:
    $ref: "./common/permissions.yaml"

"$schema": http://json-schema.org/draft-07/schema#
```

### step.yaml (Most Complex)
```yaml
title: step
oneOf:
  # Shortest: just a command string
  - type: string
    description: "Shorthand for run step"
  
  # Full step object
  - type: object
    properties:
      id:
        type: string
      name:
        type: string
      if:
        type: string
      disabled:
        type: boolean
      timeout:
        type: string
        pattern: "^([0-9]+[smhd])+$"
      
      # Step types (only one should be present)
      run:
        oneOf:
          - type: string
          - $ref: "./steps/step_run.yaml"
      action:
        $ref: "./steps/step_action.yaml"
      approval:
        $ref: "./steps/step_approval.yaml"
      background:
        $ref: "./steps/step_run.yaml"
      barrier:
        $ref: "./steps/step_barrier.yaml"
      clone:
        $ref: "./steps/step_clone.yaml"
      group:
        $ref: "./steps/step_group.yaml"
      parallel:
        $ref: "./steps/step_group.yaml"
      queue:
        $ref: "./steps/step_queue.yaml"
      template:
        $ref: "./steps/step_template.yaml"
      run-test:
        $ref: "./steps/step_test.yaml"
      
      # Control flow
      needs:
        oneOf:
          - type: string
          - type: array
            items:
              type: string
      
      strategy:
        $ref: "./common/strategy.yaml"
      
      "on-failure":
        $ref: "./common/failure.yaml"
      
      # GitHub compatibility
      env:
        type: object
        additionalProperties:
          type: string
      uses:
        type: string
      with:
        type: object

"$schema": http://json-schema.org/draft-07/schema#
```

---

## Testing Strategy

### 1. Unit Tests (JSON Schema Validation)
```bash
# Validate schema files themselves
npm install -g ajv-cli
ajv validate -s v2/pipeline/pipeline.yaml -d samples/simple.yaml
```

### 2. Sample YAML Files
Create comprehensive examples covering:
- Simple pipeline (1 stage, 1 step)
- Matrix build
- Multi-stage with dependencies
- GitHub Actions compatibility
- All step types
- All failure strategies
- Service deployment

### 3. IDE Integration Test
```json
// VS Code settings.json
{
  "yaml.schemas": {
    "file:///path/to/v2/pipeline.json": "*.v2.yaml"
  }
}
```

### 4. Conversion Tests
```bash
# Test V1 → V2 conversion
./convert-v1-to-v2.sh samples/v1/example.yaml > samples/v2/example.yaml
```

---

## Migration Path

### For Users

#### Option 1: Add `version: 2` field
```yaml
version: 2
pipeline:
  stages:
  - steps:
    - go build
```

#### Option 2: Auto-detect (if no `kind` field)
```yaml
# If root has "pipeline" key (not "version" + "kind")
# then it's V2
pipeline:
  stages: [...]
```

### For Harness Platform

1. **Parser Update**: Detect V2 syntax
2. **Execution Engine**: Support V2 execution model
3. **UI Editor**: Add V2 mode
4. **Conversion Tool**: V1 ↔ V2 converter
5. **Documentation**: Migration guides

---

## Success Metrics

1. **Schema Completeness**: 100% of bradrydzewski spec covered
2. **YAML Reduction**: >70% fewer lines for common pipelines
3. **Validation**: All sample YAMLs validate successfully
4. **IDE Support**: Autocomplete works in VS Code & IntelliJ
5. **Bundler**: Successfully generates `v2/pipeline.json`
6. **Compatibility**: GitHub Actions YAMLs work with minimal changes

---

## Timeline Estimate

- **Phase 2 (Schema Creation)**: 3-5 days
- **Phase 3 (Java Updates)**: 1-2 days
- **Phase 4 (Testing)**: 2-3 days
- **Phase 5 (Documentation)**: 1-2 days
- **Phase 6 (Integration)**: 5-10 days (platform team)

**Total**: 2-3 weeks for schema + bundler
**Platform Integration**: Additional 2-4 weeks

---

## Risks & Mitigation

### Risk 1: Breaking Changes
**Mitigation**: Keep V0/V1 intact, introduce as V2

### Risk 2: Complex Conversion Logic
**Mitigation**: Create comprehensive conversion tool with tests

### Risk 3: GitHub Compatibility Edge Cases
**Mitigation**: Test against real GitHub Actions YAMLs

### Risk 4: Performance Impact
**Mitigation**: Benchmark schema bundling and validation

---

## Next Actions

1. ✅ Create V2 directory structure
2. ✅ Write core schema files (pipeline, stage, step)
3. ✅ Write step type schemas (run, action, etc.)
4. ✅ Write common component schemas (container, strategy, etc.)
5. ✅ Update Java bundler code
6. ✅ Test schema bundling
7. ✅ Create sample YAML files
8. ✅ Validate with IDE
9. ✅ Document migration path

---

**Status**: 📋 Ready to implement | 🚀 Let's build V2!
