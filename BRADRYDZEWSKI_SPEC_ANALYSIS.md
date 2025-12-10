# Bradrydzewski Spec Analysis & Implementation Plan

## Overview
The [bradrydzewski/spec](https://github.com/bradrydzewski/spec) repository defines a **modern, simplified CI/CD pipeline specification** with GitHub Actions compatibility. It represents the next generation of Harness pipeline YAML syntax.

---

## 🎯 Key Differences from Current Harness V1

### Structure Comparison

#### Current Harness V1
```yaml
version: 1
kind: pipeline
spec:
  stages:
    - stage:
        type: CI
        spec:
          execution:
            steps:
              - step:
                  type: Run
                  spec:
                    shell: Bash
                    command: go build
```

#### Bradrydzewski Spec (Target)
```yaml
pipeline:
  stages:
  - steps:
    - run: go build
```

**Simplification: ~90% reduction in YAML verbosity!**

---

## 📊 Core Specification Structure

### 1. Pipeline Level
```typescript
interface Pipeline {
  // Identification
  id?: string;                    // Unique identifier (deprecated)
  name?: string;                  // Pipeline name (deprecated)
  
  // Configuration
  inputs?: Record<string, Input>; // Input variables
  delegate?: string | string[];   // Delegate selectors
  env?: Record<string, string>;   // Global environment
  
  // Deployment Context
  service?: ServiceRef;           // Service being deployed
  environment?: EnvironmentRef;   // Target environment (prod, staging)
  
  // Execution
  stages?: Stage[];               // Pipeline stages
  
  // Repository & Clone
  repo?: Repository;              // Override default repo
  clone?: Clone;                  // Clone configuration
  
  // Conditions & Control
  if?: string;                    // Conditional execution
  on?: On;                        // Trigger conditions
  timeout?: string;               // Pipeline timeout
  barriers?: string[];            // Pipeline barriers
  
  // GitHub Compatibility
  jobs?: Record<string, Stage>;   // GitHub: jobs
  concurrency?: Concurrency;      // Concurrency control
  permissions?: Permissions;      // Token permissions
}
```

### 2. Stage Level
```typescript
interface Stage {
  // Identification
  id?: string;
  name?: string;
  
  // Configuration
  inputs?: Input;
  
  // Execution Context
  runtime?: Runtime;              // VM, K8s, Cloud
  platform?: Platform;            // OS & architecture
  delegate?: string | string[];   // Delegate selectors
  workspace?: Workspace;          // Working directory
  
  // Deployment
  service?: ServiceRef;
  environment?: EnvironmentRef;
  
  // Steps
  steps?: Step[];                 // Execution steps
  rollback?: Step;                // Rollback steps
  
  // Stage Types (mutually exclusive)
  approval?: StageApproval;       // Approval stage
  group?: StageGroup;             // Stage group
  parallel?: StageGroup;          // Parallel stages
  template?: StageTemplate;       // Template stage
  
  // Strategies
  strategy?: Strategy;            // Matrix/loop strategy
  cache?: Cache;                  // Cache configuration
  
  // Control Flow
  if?: string;                    // Conditional execution
  disabled?: boolean;
  "on-failure"?: FailureStrategy; // Error handling
  
  // Resources
  volumes?: Volume[];
  env?: Record<string, string>;
  outputs?: Record<string, any>;  // Stage outputs
  
  // GitHub Compatibility
  needs?: string | string[];      // Dependencies
  "runs-on"?: string;             // GitHub runner
  services?: Record<string, Container>; // Background services
}
```

### 3. Step Level
```typescript
type Step = string | StepLong;  // Shorthand or full syntax

interface StepLong {
  // Identification
  id?: string;
  name?: string;
  
  // Step Types (choose one)
  run?: string | StepRun;         // Run command
  "run-test"?: StepTest;          // Run tests
  action?: StepAction;            // Run action/plugin
  approval?: StepApproval;        // Manual approval
  background?: StepRun;           // Background service
  barrier?: StepBarrier;          // Barrier step
  clone?: StepClone;              // Clone repository
  group?: StepGroup;              // Step group
  parallel?: StepGroup;           // Parallel steps
  queue?: StepQueue;              // Queue step
  template?: StepTemplate;        // Template step
  
  // Control Flow
  if?: string;                    // Conditional execution
  disabled?: boolean;
  needs?: string | string[];      // Step dependencies
  timeout?: string;
  
  // Strategy & Error Handling
  strategy?: Strategy;
  "on-failure"?: FailureStrategy;
  
  // GitHub Compatibility
  env?: Record<string, string>;
  uses?: string;                  // GitHub action
  with?: Record<string, any>;     // Action parameters
}
```

---

## 🚀 Key Features

### 1. **Multi-Syntax Support**

#### Run Step - 3 Syntaxes
```yaml
# Shortest (just the command)
- go build

# Short (run: command)
- run: go build

# Full (run object)
- run:
    script: go build
    container: golang:1.20
```

### 2. **Container Configuration**
```yaml
# Simple
container: golang

# Advanced
container:
  image: golang:1.20
  pull: if-not-exists
  privileged: false
  memory: 1gb
  cpu: 1
  env:
    GOOS: linux
```

### 3. **Matrix Strategy**
```yaml
strategy:
  matrix:
    version: ["1.19", "1.20", "1.21"]
    os: [linux, windows]
steps:
  - run:
      script: go test
      container: golang:${{ matrix.version }}
```

### 4. **Failure Strategies**
```yaml
on-failure:
  errors: all          # all, timeout, any
  action: ignore       # ignore, retry, mark-as-failure

# With retry
on-failure:
  errors: any
  action:
    retry:
      attempts: 3
      backoff: 5s
```

### 5. **Service & Environment**
```yaml
pipeline:
  service: petstore-frontend
  environment: production
  stages:
    - steps:
        - run: deploy.sh
```

### 6. **Actions (Plugin System)**
```yaml
- action:
    uses: docker-build-push-action
    with:
      push: true
      tags: latest
      repo: harness/hello-world
```

### 7. **Outputs & Variables**
```yaml
stages:
- id: stage1
  steps:
  - id: step1
    run:
      script: echo "foo=bar" >> $HARNESS_OUTPUT
  outputs:
    bar: ${{ steps.step1.outputs.foo }}
    
- steps:
  - run:
      script: echo ${{ stages.stage1.outputs.bar }}
```

### 8. **Conditional Execution**
```yaml
# Pipeline level
pipeline:
  if: ${{ branch == "main" }}
  stages: [...]

# Stage level
stages:
- if: ${{ branch == "main" }}
  steps: [...]

# Step level
steps:
- if: ${{ event == "push" }}
  run: deploy.sh
```

### 9. **Delegate Selection**
```yaml
# Inherit from infrastructure
delegate: inherit-from-infrastructure

# Specific delegates
delegate:
  - delegate-1
  - delegate-2

# Stage level
stages:
- delegate: some-delegate
  steps: [...]
```

### 10. **GitHub Actions Compatibility**
```yaml
# GitHub syntax works!
jobs:
  test:
    runs-on: ubuntu
    steps:
      - run: go build
      
# Can be extended with Harness features
jobs:
  test:
    runs-on: ubuntu
    steps:
      - run: go build
      - template:
          uses: account.docker
```

---

## 📋 Implementation Checklist

### Phase 1: Core Schema Structure ✅
- [x] Understand bradrydzewski spec
- [ ] Create new V1 schema structure matching spec
- [ ] Define Pipeline interface
- [ ] Define Stage interface
- [ ] Define Step interface

### Phase 2: Step Types
- [ ] Implement `run` step (3 syntaxes)
- [ ] Implement `action` step
- [ ] Implement `approval` step
- [ ] Implement `barrier` step
- [ ] Implement `clone` step
- [ ] Implement `group` step
- [ ] Implement `parallel` step
- [ ] Implement `queue` step
- [ ] Implement `template` step
- [ ] Implement `run-test` step
- [ ] Implement `background` step

### Phase 3: Advanced Features
- [ ] Matrix strategy
- [ ] Looping strategy (`for`, `while`)
- [ ] Failure strategies (ignore, retry, manual)
- [ ] Service references
- [ ] Environment references
- [ ] Cache configuration
- [ ] Volume configuration
- [ ] Runtime selection (VM, K8s, Cloud)

### Phase 4: Container Support
- [ ] Simple container syntax
- [ ] Advanced container configuration
- [ ] Container pull policies
- [ ] Resource limits (CPU, memory)
- [ ] Volume mounts
- [ ] Credentials

### Phase 5: Conditional Execution
- [ ] Pipeline-level `if`
- [ ] Stage-level `if`
- [ ] Step-level `if`
- [ ] Expression evaluation (${{ }})

### Phase 6: Outputs & Variables
- [ ] Step outputs
- [ ] Stage outputs
- [ ] Variable propagation
- [ ] Output masking

### Phase 7: GitHub Compatibility
- [ ] `jobs` syntax
- [ ] `runs-on` support
- [ ] `uses` / `with` for actions
- [ ] `services` for background containers
- [ ] `needs` for dependencies
- [ ] `concurrency` groups
- [ ] `permissions` management

### Phase 8: Schema Generation
- [ ] Convert TypeScript interfaces to JSON Schema
- [ ] Generate YAML schemas for each component
- [ ] Bundle schemas using existing bazel infrastructure
- [ ] Generate `v1/pipeline.json`
- [ ] Test with IDE autocomplete

---

## 🔧 Implementation Strategy

### Option 1: New V2 Directory (Recommended)
```
harness-schema/
├── v0/          # Current production
├── v1/          # Current next-gen
└── v2/          # New bradrydzewski spec
    ├── BUILD.bazel
    ├── pipeline/
    │   ├── pipeline.yaml
    │   ├── stage.yaml
    │   ├── step.yaml
    │   ├── run.yaml
    │   ├── action.yaml
    │   ├── container.yaml
    │   ├── strategy.yaml
    │   └── ...
    └── pipeline.json  # Generated
```

**Pros:**
- Clean separation
- No breaking changes
- Can coexist with V0/V1
- Easy rollback

### Option 2: Replace V1 (Breaking Change)
```
harness-schema/
├── v0/          # Legacy
└── v1/          # Replace with bradrydzewski spec
    ├── ... (new structure)
```

**Pros:**
- Single V1 version
- Clean architecture
- Forces migration

**Cons:**
- Breaking change
- Requires migration plan

---

## 📝 Sample Schema Files

### `v2/pipeline/pipeline.yaml`
```yaml
title: pipeline
type: object
"$schema": http://json-schema.org/draft-07/schema#
properties:
  id:
    type: string
    description: Unique pipeline identifier (deprecated)
  name:
    type: string
    description: Pipeline name (deprecated)
  inputs:
    type: object
    additionalProperties:
      $ref: ./input.yaml
  delegate:
    oneOf:
      - type: string
      - type: array
        items:
          type: string
  env:
    type: object
    additionalProperties:
      type: string
  service:
    $ref: ./service_ref.yaml
  environment:
    $ref: ./environment_ref.yaml
  stages:
    type: array
    items:
      $ref: ./stage.yaml
  repo:
    $ref: ./repository.yaml
  clone:
    $ref: ./clone.yaml
  if:
    type: string
  on:
    $ref: ./on.yaml
  timeout:
    type: string
    format: duration
  barriers:
    type: array
    items:
      type: string
  # GitHub compatibility
  jobs:
    type: object
    additionalProperties:
      $ref: ./stage.yaml
  concurrency:
    $ref: ./concurrency.yaml
  permissions:
    $ref: ./permissions.yaml
```

### `v2/pipeline/step.yaml`
```yaml
title: step
oneOf:
  # Shortest syntax: just a string command
  - type: string
  # Full syntax
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
      # Step types (oneOf internally)
      run:
        oneOf:
          - type: string
          - $ref: ./step_run.yaml
      action:
        $ref: ./step_action.yaml
      approval:
        $ref: ./step_approval.yaml
      background:
        $ref: ./step_run.yaml
      barrier:
        $ref: ./step_barrier.yaml
      clone:
        $ref: ./step_clone.yaml
      group:
        $ref: ./step_group.yaml
      parallel:
        $ref: ./step_group.yaml
      queue:
        $ref: ./step_queue.yaml
      template:
        $ref: ./step_template.yaml
      run-test:
        $ref: ./step_test.yaml
      # Control flow
      timeout:
        type: string
        format: duration
      needs:
        oneOf:
          - type: string
          - type: array
            items:
              type: string
      strategy:
        $ref: ./strategy.yaml
      on-failure:
        $ref: ./failure.yaml
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

### `v2/pipeline/step_run.yaml`
```yaml
title: StepRun
type: object
properties:
  shell:
    type: string
    enum: [sh, bash, powershell, pwsh, python]
  script:
    oneOf:
      - type: string
      - type: array
        items:
          type: string
  container:
    $ref: ./container.yaml
  env:
    type: object
    additionalProperties:
      type: string
  report:
    $ref: ./report_list.yaml
"$schema": http://json-schema.org/draft-07/schema#
```

### `v2/pipeline/container.yaml`
```yaml
title: container
oneOf:
  # Simple: just image name
  - type: string
  # Advanced configuration
  - type: object
    properties:
      image:
        type: string
      connector:
        type: string
      pull:
        type: string
        enum: [always, never, if-not-exists]
      privileged:
        type: boolean
      user:
        oneOf:
          - type: string
          - type: number
      group:
        oneOf:
          - type: string
          - type: number
      cpu:
        oneOf:
          - type: string
          - type: number
      memory:
        oneOf:
          - type: string
          - type: number
      env:
        type: object
        additionalProperties:
          type: string
      entrypoint:
        oneOf:
          - type: string
          - type: array
            items:
              type: string
      args:
        oneOf:
          - type: string
          - type: array
            items:
              type: string
      workdir:
        type: string
      network:
        type: string
      network-mode:
        type: string
      volumes:
        type: array
        items:
          $ref: ./mount.yaml
"$schema": http://json-schema.org/draft-07/schema#
```

---

## 🎓 Migration Examples

### Before (Current Harness V1)
```yaml
version: 1
kind: pipeline
spec:
  stages:
    - stage:
        identifier: build
        name: Build
        type: CI
        spec:
          cloneCodebase: true
          execution:
            steps:
              - step:
                  identifier: build_app
                  name: Build Application
                  type: Run
                  spec:
                    shell: Bash
                    command: |
                      go build
                      go test
                    envVariables:
                      GOOS: linux
                      GOARCH: amd64
```

### After (Bradrydzewski Spec)
```yaml
pipeline:
  stages:
  - id: build
    name: Build
    steps:
    - id: build_app
      name: Build Application
      run:
        script:
        - go build
        - go test
        env:
          GOOS: linux
          GOARCH: amd64
```

**~70% less YAML!**

---

## ✅ Next Steps

1. **Create V2 directory structure**
2. **Convert TypeScript interfaces to YAML schemas**
3. **Implement core components** (pipeline, stage, step)
4. **Add step types** (run, action, etc.)
5. **Test schema bundling** with existing bazel infrastructure
6. **Create sample YAML files** for validation
7. **Update IDE integration** (VS Code, IntelliJ)
8. **Document migration path** from V0/V1 to V2

---

## 📚 Resources

- **Spec Repository**: https://github.com/bradrydzewski/spec
- **Schema Directory**: https://github.com/bradrydzewski/spec/tree/master/schema
- **Sample YAMLs**: https://github.com/bradrydzewski/spec/tree/master/samples
- **Go Parser**: https://github.com/bradrydzewski/spec/tree/master/yaml

---

## 🤝 Benefits

1. **Simpler YAML** - 70-90% reduction in verbosity
2. **GitHub Compatible** - Easy migration from GitHub Actions
3. **Modern Syntax** - Follows industry best practices
4. **Flexible** - Multi-syntax support (short/long forms)
5. **Extensible** - Plugin/action system
6. **Type Safe** - Strong schema validation
7. **Developer Friendly** - Intuitive structure
8. **Future Proof** - Designed for next-gen CI/CD

---

**Status**: ✅ Analysis Complete | 📋 Ready for Implementation
