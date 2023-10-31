const propertyTemplate = `
## \`{property}\`

{desc}

- Type: {type}
{formatLine}
- Required: {required}

{exampleSection}
{oneOfSection}
`;

const childTemplate = `
\`\`\`json
{schema}
\`\`\`
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
    schemaTemplate,
    childTemplate
  };