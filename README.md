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

(<img width="1161" alt="workspace" src="https://github.com/harness/harness-schema/assets/67271707/b0167760-b665-494e-aa15-64c355b61af1">
)

3. Select option __import from project view__ and give path as __bundler/project/bazelproject__ and click __next__

4. Harness Schema Repo is ready for use
