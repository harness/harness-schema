# Create Pipeline Step

Create or update a pipeline step schema in the harness-schema repository.

Use the `harness-step-schema` skill to guide you through the process.

## Instructions

Read and follow the skill at `.claude/skills/harness-step-schema/SKILL.md`.

For templates, refer to `.claude/skills/harness-step-schema/TEMPLATES.md`.

For examples, refer to `.claude/skills/harness-step-schema/EXAMPLES.md`.

## Process

1. Ask the user for: step name, category, fields, target stages, and whether to include in templates
2. Create the step info YAML file
3. Create the step node YAML file
4. Update stage execution-wrapper-config.yaml files
5. Update template_config.yaml if templates enabled
6. Update pipeline.json with definitions and references
7. Update template.json with definitions and references
8. Validate JSON files are valid

$ARGUMENTS
