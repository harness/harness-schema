const fs = require('fs');

function readJsonSchema(file) {
  try {
    const content = fs.readFileSync(file, 'utf8');
    return JSON.parse(content);
  } catch (error) {
    console.error(`Error reading JSON schema file: ${error.message}`);
    process.exit(1);
  }
}

function generateMarkdown(schema, options = {}) {
    const { propertyTemplate = '', schemaTemplate = '' } = options;
  
    let properties = '';
  
    for (const property in schema.properties) {
      const { type, format, description, examples } = schema.properties[property];
      const required = schema.required.includes(property) ? 'Yes' : 'No';
      const formatLine = format ? `- Format: ${format}` : '';
  
      const exampleBlocks = examples
        ? examples
            .map((example) => `\`\`\`json\n${JSON.stringify(example, null, 2)}\n\`\`\``)
            .join('\n\n')
        : 'No examples provided.';
  
      properties += propertyTemplate
        .replace(/\{property\}/g, property)
        .replace(/\{type\}/g, type)
        .replace(/\{formatLine\}/g, formatLine)
        .replace(/\{required\}/g, required)
        .replace(/\{description\}/g, description)
        .replace(/\{examples\}/g, exampleBlocks);
    }
  
    return schemaTemplate
      .replace(/\{title\}/g, schema.title)
      .replace(/\{properties\}/g, properties)
      .replace(/\{schema\}/g, JSON.stringify(schema, null, 2));
  }
  
  

const schema = readJsonSchema('testschema.json');
const propertyTemplate = `
## \`{property}\`

{description}

- Type: {type}
{formatLine}
- Required: {required}

### Examples

{examples}
`;


const schemaTemplate = `
# {title}

A representation of a user object.
{properties}
## JSON Schema

\`\`\`json
{schema}
\`\`\`
`;


const markdown = generateMarkdown(schema, { propertyTemplate, schemaTemplate });
fs.writeFileSync('userSchema.md', markdown, 'utf8');
console.log('Markdown file generated: userSchema.md');
