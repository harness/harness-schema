const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');
const { propertyTemplate, schemaTemplate , childTemplate} = require('./templates');

if (process.argv.length < 4) {
  console.error('Usage: node generateMarkdown.js <input_json_schema> <output_md_file>');
  process.exit(1);
}

const inputSchemaPath = path.resolve(process.argv[2]);
const outputMarkdownPath = path.resolve(process.argv[3]);

function readJsonSchema(file) {
  try {
    const content = fs.readFileSync(file, 'utf8');
    return JSON.parse(content);
  } catch (error) {
    console.error(`Error reading JSON schema file: ${error.message}`);
    process.exit(1);
  }
}

function generateMarkdown(schema, options = {}, child = false) {
    const { propertyTemplate = '', schemaTemplate = '' , childTemplate = ''} = options;

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
      const { type, format, desc, examples, oneOf } = schema.properties[property];
      const required = schema.required.includes(property) ? 'Yes' : 'No';
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
      const oneOfNotNull = oneOf ? oneOf : '';

        if (oneOf) {
            // Handle "oneOf" properties
            const oneOfDescriptions = oneOf.map((subSchema, index) => {
                const subProperties = generateMarkdown(subSchema, options, true);
                return `#### Option ${index + 1}:\n${subProperties}`;
            });

            // Update existing property documentation
            properties += propertyTemplate
                .replace(/\{property\}/g, property)
                .replace(/\{type\}/g, 'oneOf') // Customize the type label
                .replace(/\{formatLine\}/g, formatLine)
                .replace(/\{required\}/g, required)
                .replace(/\{desc\}/g, descNotNull)
                .replace(/\{exampleSection\}/g, exampleSection)
                .replace(/\{oneOfSection\}/g, oneOfDescriptions.join('\n'));

            // Append "oneOf" descriptions to the existing property documentation
            // properties += oneOfDescriptions.join('\n');
        } else {
        properties += propertyTemplate
            .replace(/\{property\}/g, property)
            .replace(/\{type\}/g, type)
            .replace(/\{formatLine\}/g, formatLine)
            .replace(/\{required\}/g, required)
            .replace(/\{desc\}/g, descNotNull)
            .replace(/\{exampleSection\}/g, exampleSection)
            .replace(/\{oneOfSection\}/g, oneOfNotNull);
        }
    }

    if (schema.allOf) {
        for (const subSchema of schema.allOf) {
            if (subSchema.if && subSchema.then && subSchema.then.properties) {
                for (const subProperty in subSchema.then.properties) {
                    // Append sub-properties from "if" and "then" clauses
                    console.log(subProperty);
                    const { type, format, desc, examples, oneOf } = subSchema.then.properties[subProperty];
                    const required = schema.required.includes(subProperty) ? 'Yes' : 'No';
                    const formatLine = format ? `- Format: ${format}` : '';
                    const descNotNull = desc ? desc : '';
                    const oneOfNotNull = oneOf ? oneOf : '';
                    const exampleBlocks = examples
                        ? examples
                            .map((example) => `\`\`\`yaml\n${yaml.dump(example)}\`\`\``)
                            .join('\n\n')
                        : '';

                    const exampleSection = examples
                        ? `### Examples\n\n${exampleBlocks}`
                        : '';
                    properties += propertyTemplate
                        .replace(/\{property\}/g, subProperty)
                        .replace(/\{type\}/g, type)
                        .replace(/\{formatLine\}/g, formatLine)
                        .replace(/\{required\}/g, required)
                        .replace(/\{desc\}/g, descNotNull)
                        .replace(/\{oneOfSection\}/g, oneOfNotNull)
                        .replace(/\{exampleSection\}/g, exampleSection);
                }
            }
        }
    }

  if(!child){
      return schemaTemplate
          .replace(/\{title\}/g, schema.title)
          .replace(/\{stepYaml\}/g, titleExampleSection)
          .replace(/\{properties\}/g, properties)
          .replace(/\{schema\}/g, JSON.stringify(schema, null, 2));
  }

    return childTemplate
      .replace(/\{schema\}/g, JSON.stringify(schema, null, 2));
  }
  

const schema = readJsonSchema(inputSchemaPath);

const markdown = generateMarkdown(schema, { propertyTemplate, schemaTemplate, childTemplate });
fs.writeFileSync(outputMarkdownPath, markdown, 'utf8');
console.log('Markdown file generated', outputMarkdownPath);
