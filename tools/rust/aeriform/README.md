Aeriform - forming the proper layering for your java project 
==================================================
## Prerequisities
1. Prapare the portal project based on the instructions in it readme 

2. Install rust:
https://www.rust-lang.org/tools/install

3. Install rust plugin for IntelliJ:
https://plugins.jetbrains.com/plugin/8182-rust

## Disclamer:
This is a harness homemade tool. It makes a lot of assumptions for the structure of the code.
It is not extensively tested and any deviation from the common setup will most likely require
fixing.

## How it works
The tool detects and report possible actions, warnings and errors with the layering, based on
the annotation @TargetModule(HarnessModule.xxx) of the classes in the modules. This suggested target
module is analyzed against the dependencies of the classes and issues are reported.

The tool assumes that every class without an annotation is purposed to stay in the current module.

## How to use it

The tool assumes complete match between the code base and the bazel artifacts. Make sure to execute
```
bazel build //...
```
form the root of the repo. 

Note that adding/removing or changing annotations does not require rebuild. Any class 
adding/renaming/movment/deletion or change requires build. 

You can execute the tool from IntelliJ, or from cli with:

```
cargo run --color=always --release --package aeriform --bin aeriform
```

For more details read the internal help that will be printed.