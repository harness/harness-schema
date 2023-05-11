// Copyright 2021 Harness Inc. All rights reserved.
// Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
// that can be found in the licenses directory at the root of this repository, also available at
// https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

use clap::Clap;

use crate::analyze::{analyze, Analyze};
use crate::execute::{execute, Execute};
use crate::prepare::{prepare, Prepare};

mod analyze;
#[path = "execute/execute.rs"]
mod execute;
#[path = "execute/execute_apply_target.rs"]
mod execute_apply_target;
#[path = "execute/execute_class_move.rs"]
mod execute_class_move;
mod prepare;

mod java_class;
mod java_module;
mod repo;
mod team;

#[derive(Clap)]
#[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
struct Opts {
    /// A level of verbosity, and can be used multiple times
    #[clap(short, long, parse(from_occurrences))]
    verbose: i32,
    #[clap(subcommand)]
    subcmd: SubCommand,
}

#[derive(Clap)]
enum SubCommand {
    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    Analyze(Analyze),

    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    Execute(Execute),

    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    Prepare(Prepare),
}

fn main() {
    let opts: Opts = Opts::parse();

    // Vary the output based on how many times the user used the "verbose" flag
    // (i.e. 'myprog -v -v -v' or 'myprog -vvv' vs 'myprog -v'
    match opts.verbose {
        0 => (),
        1 => println!("Some verbose info"),
        2 => println!("Tons of verbose info"),
        3 | _ => println!("Don't be crazy"),
    }

    // You can handle information about subcommands by requesting their matches by name
    // (as below), requesting just the name used, or both at the same time
    match opts.subcmd {
        SubCommand::Analyze(_options) => {
            analyze(_options);
        }
        SubCommand::Execute(_options) => {
            execute(_options);
        }
        SubCommand::Prepare(_options) => {
            prepare(_options);
        }
    }

    // more program logic goes here...
}
