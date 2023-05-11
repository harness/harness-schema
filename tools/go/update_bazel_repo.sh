#!/usr/bin/env bash
# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

##########################################################################################

#We are using bazel for building and testing go source code
#bazel needs BUILD.bazel files and doesn't need go.mod or go.sum files
#go.mod and go.sum files  are needed for native go tools

#bazel uses $PROJECT_ROOT/WORKSPACE to store go repositories
# which has information found in both go.mod and go.sum
#bazel uses $PROJECT_ROOT/WORKSPACE for dependency management
#and BUILD.bazel for actual execution of targets

#To use local modules in an application
#for eg., to use module `lib` in an application `ci-addon`,
#we need to perform these actions:

#$ cd ci/addon #cd to the application folder where main.go is located
#$ go mod init <module-name>   <-- this generates go.mod and go.sum
#update go.mod by adding the following line to point to local repository:
#  replace github.com/harness/harness-core/commons/go/lib => ../../../commons/go/lib
#(the above replace is needed only if you are outside the module you want to import)
#go get <-- this updates go.mod

#gazelle <-- generates, updates BUILD.bazel

#To update go_repository() at portal/WORKSPACE, run this script as:

#portal/tools/go/update_bazel_repo.sh go.mod

#bazel doesn't import local repository and hence this script temporarily removes
#local repository dependency, updates WORKSPACE and then restores original go.mod

##########################################################################################
set -euo pipefail

# The name of the account for which we don't want to download the dependencies from the internet
# Instead we use the local checked-out code.
# This is to make both bazel and go native tools work
PROJECT_GITHUB_ACCOUNT=wings-software
PROJECT_ROOT="${PROJECT_ROOT:-$(git rev-parse --show-toplevel)}"

path_to_go_mod=$PWD/$1

path_to_go_mod=$PWD/$1 path_to_go_mod=$PWD/$1
cp $path_to_go_mod $path_to_go_mod.bak

grep module $path_to_go_mod.bak > $path_to_go_mod  # First store the module name
# Remove any requirement on local module
grep -v module $path_to_go_mod.bak | grep -v $PROJECT_GITHUB_ACCOUNT >> $path_to_go_mod


# Now update the go_repository() at $PROJECT_ROOT/WORKSPACE
cd $PROJECT_ROOT
bazelisk run //:gazelle -- update-repos -from_file=$path_to_go_mod

# Now restore the original go.mod to support native go tools
mv $path_to_go_mod.bak $path_to_go_mod

echo "$PROJECT_ROOT/WORKSPACE is updated"
