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

Our pipeline YAML schema allows users to define their pipelines, making it easier to specify the steps, dependencies, and configurations required for their projects. With this schema, users can seamlessly create and modify pipeline configurations, leading to more efficient and error-free deployments.

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

---

