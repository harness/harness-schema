---
name: harness-step-schema
description: |
  Creates or updates pipeline step schemas in the harness-schema repository.

  ALWAYS use this skill when user mentions ANY of these:
  - Step operations: "create step", "add step", "new step", "update step", "modify step", "edit step", "implement step", "define step", "build step"
  - Schema files: "step-info.yaml", "step-node.yaml", "StepInfo", "StepNode", "step schema", "step definition", "step spec"
  - Stage availability: "add to stage", "enable in stage", "available in stage", "register step", "execution-wrapper-config"
  - Template support: "step template", "template_config.yaml", "enable as template"
  - JSON updates: "pipeline.json", "template.json", "add definition", "update schema"
  - Field work: "add field", "add property", "new field", "step property", "step field", "modify field"
  - Expression support: "runtime input", "JEXL", "expression support", "<+input>", "common-jexl"
  - Category mentions: "CD step", "CI step", "custom step", "FME step", "feature flag step", "approval step"

  If user is working with files in v0/pipeline/steps/ or mentions step-related schema changes, USE THIS SKILL.
---

# Harness Pipeline Step Schema Management

This skill helps create or update pipeline step schemas in the harness-schema repository.

**Reference Documentation:**
- `v0/AGENTS.md` - Directory structure, common schema files, and field patterns
- `TEMPLATES.md` - Full YAML/JSON templates for step creation
- `EXAMPLES.md` - Existing step examples

## Before Starting - Ask These Questions

When creating a new step, gather this information from the user:

1. **Step name**: PascalCase name (e.g., `FmeFlagDelete`, `MyCustomStep`)
2. **Step category**: Where to place it? (see `v0/AGENTS.md` for directory structure)
   - `cd` - CD (Continuous Deployment) specific steps
   - `ci` - CI (Continuous Integration) specific steps
   - `common` - Shared across multiple stages
   - `custom` - Custom steps (approvals, FME flags, HTTP, etc.)
   - `cvng` - Continuous Verification steps
   - `iacm` - Infrastructure as Code Management steps
   - `idp` - Internal Developer Portal steps

3. **Step fields**: What properties does the step spec need?
   - Field name, type, required/optional, description
   - Should fields support runtime input (`<+input>`) and expressions?
   - See `v0/AGENTS.md` "Common Schema Files" section for available field patterns

4. **Target stages**: Which stages should have this step? (select multiple)
   - `approval` - Approval stage
   - `cd` - Deployment stage
   - `cf` - Feature Flag stage
   - `ci` - Build/CI stage
   - `custom` - Custom stage
   - `iacm` - IACM stage
   - `idp` - IDP stage
   - `security` - Security stage

   **Note:** Steps can be added to multiple stages. See `v0/AGENTS.md` "Stage-Step Availability" for common patterns.

5. **Include in templates?**: Should this step be available as a step template?

## File Structure

Each step requires these files:

### 1. Step Info YAML
`v0/pipeline/steps/{category}/{step-name}-step-info.yaml`

Defines the step's spec properties. See `v0/AGENTS.md` "Common Field Patterns" for available patterns.

### 2. Step Node YAML
`v0/pipeline/steps/{category}/{step-name}-step-node.yaml`

Defines the step wrapper with type, identifier, name, timeout, etc.

### 3. Stage Execution Configs
`v0/pipeline/stages/{stage}/execution-wrapper-config.yaml`

Add reference to make step available in each target stage.

### 4. Template Config (if templates enabled)
`v0/template/template_config.yaml`

Add to `step_template_types:` list.

### 5. Bundled JSON Files
- `v0/pipeline.json` - Add definitions + stage references
- `v0/template.json` - Add definitions + template/stage references

## Step Creation Process

1. Create `{step-name}-step-info.yaml` in `v0/pipeline/steps/{category}/`
2. Create `{step-name}-step-node.yaml` in `v0/pipeline/steps/{category}/`
3. Add reference to each target stage's `execution-wrapper-config.yaml`
4. Add to `v0/template/template_config.yaml` if templates enabled
5. Add definitions to `v0/pipeline.json`:
   - `{StepName}StepNode` definition
   - `{StepName}StepInfo` definition
   - References in stage ExecutionWrapperConfig oneOf arrays
6. Add definitions to `v0/template.json`:
   - `{StepName}StepNode_template` definition (no identifier/name required)
   - `{StepName}StepNode` definition
   - `{StepName}StepInfo` definition
   - Reference in step template spec oneOf
   - References in stage ExecutionWrapperConfig oneOf arrays

## Reference Files

- **v0/AGENTS.md** - Common schema files, field patterns, directory structure
- **TEMPLATES.md** - YAML/JSON templates for step definitions
- **EXAMPLES.md** - Existing step examples (FmeFlagDelete, FmeFlagCreate)
