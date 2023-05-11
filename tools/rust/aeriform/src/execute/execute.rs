// Copyright 2021 Harness Inc. All rights reserved.
// Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
// that can be found in the licenses directory at the root of this repository, also available at
// https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

use clap::Clap;
use std::fs::File;
use std::io;
use std::io::{BufRead, BufReader};
use std::path::Path;

use crate::execute_apply_target::{apply_target, ApplyTarget};
use crate::execute_class_move::{move_class, MoveClass};

#[derive(Clap)]
enum Action {
    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    MoveClass(MoveClass),

    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    ApplyTarget(ApplyTarget),
}

/// A sub-command to analyze the project module targets and dependencies
#[derive(Clap)]
pub struct Execute {
    /// Filter the reports by affected module module_filter.
    #[clap(subcommand)]
    action: Action,
}

pub fn execute(opts: Execute) {
    match opts.action {
        Action::MoveClass(action_opts) => {
            move_class(action_opts);
        }
        Action::ApplyTarget(action_opts) => {
            apply_target(action_opts);
        }
    }
}

pub fn read_lines<P>(filename: P) -> io::Result<io::Lines<BufReader<File>>>
where
    P: AsRef<Path>,
{
    let file = File::open(filename)?;
    Ok(BufReader::new(file).lines())
}
