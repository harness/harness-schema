# Harness NG - Pipeline YAML Schema

Welcome to the GitHub repository of our Harness NextGen Pipeline YAML Schema! This repository contains the schema definition for our pipeline configuration files, enabling users to define and customize their pipelines effortlessly.

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Usage](#usage)
    - [Embedding the Schema in Visual Studio Code](#embedding-the-schema-in-visual-studio-code)
    - [Embedding the Schema in Sublime Text](#embedding-the-schema-in-sublime-text)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Our pipeline YAML schema allows users to define their pipelines, making it easier to specify the steps, dependencies, and configurations required for their projects. With this schema, users can seamlessly create and modify pipeline configurations, resulting in more efficient and error-free deployments.

## Getting Started

### Installation

To start using the pipeline YAML schema, you need to follow these simple steps:

1. **Clone the Repository**: Begin by cloning this GitHub repository to your local machine using the following command:

```bash
git clone https://github.com/harness/harness-schema.git
```

2. **Integration with Your Text Editor**: To enable auto-intellisense for the pipeline YAML schema in your preferred text editors, follow the instructions below.

### Usage

#### Embedding the Schema in Visual Studio Code

1. **Install the YAML Extension**: Make sure you have the "YAML" extension installed in your Visual Studio Code editor.

2. **Open Your User Settings**: In Visual Studio Code, go to **File > Preferences > Settings**.

3. **Configure User Settings**: In the right-hand pane, click on the `{}` icon to edit the settings.json file.

4. **Add Schema Association**: To enable auto-intellisense for our pipeline YAML schema, add the following lines to your settings.json file:

```json
"yaml.schemas": {
    "https://raw.githubusercontent.com/harness/harness-schema/main/v0/pipeline.json": "pipeline.yaml"
}
```

5. **Restart Visual Studio Code**: After adding the schema association, restart Visual Studio Code for the changes to take effect.

Now, when you work on a pipeline YAML file, Visual Studio Code will provide auto-intellisense based on the defined schema, helping you write correct and valid pipeline configurations effortlessly.

#### Embedding the Schema in Sublime Text

1. **Install Package Control**: If you haven't already, install Package Control for Sublime Text. You can find installation instructions [here](https://packagecontrol.io/installation).

2. **Install YAML Extension**: In Sublime Text, open the command palette by pressing `Ctrl + Shift + P` (Windows/Linux) or `Cmd + Shift + P` (macOS). Type "Package Control: Install Package" and hit `Enter`. Search for the "YAML" package and install it.

3. **Add Schema Association**: Once you have the YAML package installed, go to **Preferences > Package Settings > YAML > Settings**.

4. **Configure User Settings**: In the right-hand pane, add the following configuration to enable auto-intellisense for our pipeline YAML schema:

```json
{
    "schema": "https://raw.githubusercontent.com/harness/harness-schema/main/v0/pipeline.json"
}
```

5. **Restart Sublime Text**: After adding the schema association, restart Sublime Text for the changes to take effect.

Now, Sublime Text will provide auto-intellisense for the pipeline YAML schema, making it easier for you to write correct pipeline configurations.

## Contributing

We welcome contributions to enhance the pipeline YAML schema and make it even more robust. If you find any issues or have suggestions for improvement, please feel free to create an issue or submit a pull request. For major changes, please discuss your ideas with the maintainers first.

### Release & Testing Process

when we do branch cut for pipeline service, then we merge latest pipeline.json ( from schema repo ) into develop and release branch, you can verify your changes in release branch and develop once branch cut is done 
Similarly we when we do branch cut for template service, then we merge latest template.json ( from schema repo ) into develop and release branch, we can verify our intended changes in release branch and develop once branch cut is done

Also currently in Devspace the schema changes reflect instantaneously, as in devspaces we fetch schema directly from the harness-schema repo ( without the need to deploy / bounce devspace )
We have a flag USE_SCHEMA_FROM_HARNESS_SCHEMA_REPO whenever this flag is true we always fetch the schema from schema repo ( we do not take local pipeline.json into consideration here ) similarly whenever this flag is set to false we always use schema from local ( pipeline.json in pipeline src , template.json in template src )

USE_SCHEMA_FROM_HARNESS_SCHEMA_REPO this flag is true for devspace by default ( if you wish to test out your changes in devspace you will need to set this config to false )
For all other env this flag is false by default therefore local ( pipeline.json in pipeline src , template.json in template src ) are picked on PROD, QA and local env

For testing schema changes in local we can paste the generated schema from schema repo in local ( v0/pipeline.json and v0/template.json ) and we will need to restart pipeline/template src for this schema changes to be reflected, then go to YAML editor in pipeline/template studio the changes should be reflected there!

## Command to generate schema

```bash
bazel run bundler/schema_store:module
```

