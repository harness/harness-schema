const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');
const { propertyTemplate, schemaTemplate } = require('./templates');

if (process.argv.length < 4) {
  console.error('Usage: node generateMarkdown.js <input_json_schema> <output_md_file>');
  process.exit(1);
}

const inputSchemaPath = path.resolve(process.argv[2]);
const outputMarkdownPath = path.resolve(process.argv[3]);

//function readJsonSchema(file) {
//  try {
//    const content = fs.readFileSync(file, 'utf8');
//    return JSON.parse(content);
//  } catch (error) {
//    console.error(`Error reading JSON schema file: ${error.message}`);
//    process.exit(1);
//  }
//}

function readJsonSchema(file) {
  try {
    const content = fs.readFileSync(file, 'utf8');
    const schema = JSON.parse(content);
    console.log('Schema:', schema);
    return schema;
  } catch (error) {
    console.error(`Error reading JSON schema file: ${error.message}`);
    const match = error.message.match(/at position (\d+)/);
    if (match) {
      const pos = parseInt(match[1]);
      const start = Math.max(pos - 20, 0);
      const context = content.substr(start, 40);
      console.error(`Error context: ...${context}...`);
    }
    process.exit(1);
  }
}

function generateMarkdown(schema, options = {}) {
    const { propertyTemplate = '', schemaTemplate = '' } = options;

    const titleExampleBlock = schema.examples
        ? schema.examples
            .map((example) => `\`\`\`yaml\n${yaml.dump(example)}\`\`\``)
            .join('\n\n')
        : '';

    
    const titleExampleSection = schema.examples
        ? `### Yaml\n\n${titleExampleBlock}`
        : '';
  
    let properties = '';
  
    for (const property in schema.properties) {
      const { type, format, desc, examples } = schema.properties[property];
      const required = schema.required && schema.required.includes(property) ? 'Yes' : 'No';

      const formatLine = format ? `- Format: ${format}` : '';
  
      const exampleBlocks = examples
        ? examples
            .map((example) => `\`\`\`yaml\n${yaml.dump(example)}\`\`\``)
            .join('\n\n')
        : '';
  
      const exampleSection = examples
        ? `### Examples\n\n${exampleBlocks}`
        : '';

      const descNotNull = desc ? desc : '';

      properties += propertyTemplate
        .replace(/\{property\}/g, property)
        .replace(/\{type\}/g, type)
        .replace(/\{formatLine\}/g, formatLine)
        .replace(/\{required\}/g, required)
        .replace(/\{desc\}/g, descNotNull)
        .replace(/\{exampleSection\}/g, exampleSection);
    }
  
    return schemaTemplate
      .replace(/\{title\}/g, schema.title)
      .replace(/\{stepYaml\}/g, titleExampleSection)
      .replace(/\{properties\}/g, properties)
      .replace(/\{schema\}/g, JSON.stringify(schema, null, 2));
  }
  

const schema = readJsonSchema(inputSchemaPath);

const markdown = generateMarkdown(schema, { propertyTemplate, schemaTemplate });
fs.writeFileSync(outputMarkdownPath, markdown, 'utf8');
console.log('Markdown file generated', outputMarkdownPath);
