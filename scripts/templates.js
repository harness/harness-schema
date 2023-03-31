const propertyTemplate = `
## \`{property}\`

{desc}

- Type: {type}
{formatLine}
- Required: {required}

{exampleSection}
`;


const schemaTemplate = `
# {title}

{stepYaml}

Harness Wait Step Yaml schema.
{properties}
## JSON Schema

\`\`\`json
{schema}
\`\`\`
`;


module.exports = {
    propertyTemplate,
    schemaTemplate
  };