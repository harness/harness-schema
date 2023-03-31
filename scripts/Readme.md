# JSON Schema to Markdown Documentation Generator

This utility converts JSON schema files into Markdown documentation files. It reads a JSON schema file, extracts the properties, and generates human-readable documentation in Markdown format.

## Setup

To set up the project, follow these steps:

1. Clone the repository and navigate to scripts folder.

2. Install the required dependencies:

~~~bash
npm install
~~~

## How to Run

Navigate to scripts folder & run the following command:

~~~bash
node generateMarkdown.js <input_json_schema> <output_md_file>
~~~

Replace `<input_json_schema>` with the path to your JSON schema file, and `<output_md_file>` with the desired output path for the Markdown documentation file.

~~~bash
node generateMarkdown.js wait.json waitSchema.md
~~~

## File Structure

The project consists of the following files:

- `generateMarkdown.js`: The main script responsible for converting JSON schema files into Markdown documentation files. It takes input and output file paths as command line arguments and processes the JSON schema file accordingly.

- `templates.js`: Contains the templates for property and schema sections used in the generated Markdown file. You can customize these templates to modify the appearance of the output.


## Customizing Templates

To customize the appearance of the generated Markdown documentation, you can modify the templates in the `templates.js` file. It contains two templates:

- `propertyTemplate`: Defines the format for individual properties in the JSON schema. You can modify this template to change how properties are displayed in the documentation.

- `schemaTemplate`: Defines the overall structure of the generated Markdown file. You can modify this template to change the layout and appearance of the documentation.

Remember to keep the placeholders (e.g., `{property}`, `{type}`) intact while making changes to the templates, as these are replaced with actual values during the generation process.
