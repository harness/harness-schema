#!/usr/bin/env bash
# Copyright 2021 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

##########################################################################################

# Run this script to download the required Go tools for Go development


##########################################################################################
set -euo pipefail
PROJECT_ROOT="${PROJECT_ROOT:-$(git rev-parse --show-toplevel)}"

echo $PROJECT_ROOT
cd $PROJECT_ROOT
go get -u github.com/mdempsky/gocode github.com/uudashr/gopkgs/v2/cmd/gopkgs github.com/ramya-rao-a/go-outline github.com/acroca/go-symbols golang.org/x/tools/cmd/guru golang.org/x/tools/cmd/gorename github.com/cweill/gotests github.com/fatih/gomodifytags github.com/josharian/impl github.com/davidrjenni/reftools/cmd/fillstruct github.com/haya14busa/goplay/cmd/goplay github.com/godoctor/godoctor github.com/stamblerre/gocode github.com/rogpeppe/godef github.com/sqs/goreturns golang.org/x/lint/golint github.com/golang/mock/gomock golang.org/x/tools/go/packages github.com/wadey/gocovmerge github.com/bazelbuild/bazel-gazelle/cmd/gazelle

go install github.com/golang/mock/mockgen
