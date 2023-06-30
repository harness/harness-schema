## Overview
Onboarding to Static Schema - [On-boarding](https://harness.atlassian.net/wiki/spaces/PIPE/pages/21390131775/Static+Schema+Onboarding)

Developer Guide - [Developer Guide](https://harness.atlassian.net/wiki/spaces/PIPE/pages/21420410346/Developer+Guide+for+Static+Schema)

Static Schema Support For Different Environment - [Environment Support](https://harness.atlassian.net/wiki/spaces/PIPE/pages/21419919199/Making+Static+Schema+Live+in+Different+Environment)


# **Local Development with Static Schema**

1. Clone Repository:

  ```
git clone https://github.com/harness/harness-schema.git"
  ```

2. Create a __.bazelrc__ file under root directory

``` touch .bazelrc ```

3. Add the following line __.bazelrc__ file

```import bundler/bazelrc.common```


# **IntelliJ Setup**

1. Open the intelliJ and choose option - __Import Bazel Project__
2. Choose the harness-schema directory in option - *workspace* and click *next*

<img width="1161" alt="workspace" src="https://github.com/harness/harness-schema/assets/67271707/b0167760-b665-494e-aa15-64c355b61af1">


3. Select option __import from project view__ and give path as __bundler/project/bazelproject__ and click __next__

  <img width="1146" alt="projectView" src="https://github.com/harness/harness-schema/assets/67271707/2e5dfe42-8332-4ea8-aa02-cff623dd202d">


5. Harness Schema Repo is ready for use
