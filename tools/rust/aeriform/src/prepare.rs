// Copyright 2021 Harness Inc. All rights reserved.
// Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
// that can be found in the licenses directory at the root of this repository, also available at
// https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

use clap::Clap;
use multimap::MultiMap;
use std::fs::File;
use std::io::{Result, Write};

use crate::java_class::JavaClassTraits;
use crate::java_module::{model_names, modules};
use crate::repo::GIT_REPO_ROOT_DIR;

#[derive(Clap)]
pub struct BazelRule {}

#[derive(Clap)]
pub struct TeamClasses {
    /// Output to files.
    #[clap(short, long)]
    root_location: Option<String>,
}

#[derive(Clap)]
enum PrepareSubCommand {
    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    BazelRule(BazelRule),

    #[clap(version = "1.0", author = "George Georgiev <george@harness.io>")]
    TeamClasses(TeamClasses),
}

/// A sub-command to prepare the bazel rules that build
#[derive(Clap)]
pub struct Prepare {
    #[clap(subcommand)]
    subcmd: PrepareSubCommand,
}

pub fn prepare(opts: Prepare) {
    match opts.subcmd {
        PrepareSubCommand::BazelRule(_options) => match generate_bazel_rule() {
            Ok(_) => {}
            Err(err) => {
                println!("Failed to generate bazel rule with error: {}", err);
            }
        },
        PrepareSubCommand::TeamClasses(_options) => match generate_team_classes(_options) {
            Ok(_) => {}
            Err(err) => {
                println!("Failed to generate team classes with error: {}", err);
            }
        },
    }
}

fn generate_bazel_rule() -> Result<()> {
    println!("preparing...");

    let modules = model_names();

    let target_file = format!("{}/.aeriform/BUILD.bazel", GIT_REPO_ROOT_DIR.as_str());
    let mut target = File::create(&target_file)?;

    writeln!(target, "load(\"//:tools/bazel/aeriform.bzl\", \"aeriform\")")?;

    let mut vec = modules.keys().collect::<Vec<&String>>();
    vec.sort();

    for module in vec {
        writeln!(target, "")?;
        writeln!(target, "aeriform(\"{}\")", module)?;
    }

    Ok(())
}

fn generate_team_classes(opts: TeamClasses) -> Result<()> {
    let modules = modules();

    let mut team_classes: MultiMap<String, &String> = MultiMap::new();

    modules.values().for_each(|module| {
        module.srcs.iter().for_each(|src| {
            team_classes.insert(src.1.team(module, &src.1.target_module_team(&modules)).clone(), src.0);
        });
    });

    for (team, classes) in team_classes.iter_all_mut() {
        classes.sort();

        match &opts.root_location {
            None => {
                println!("{}", team);
                classes.iter().for_each(|&class| println!("    {}", class));
            }
            Some(root_location) => {
                let target_file = format!("{}{}.txt", root_location, team);
                let mut target = File::create(&target_file)?;

                for class in classes {
                    writeln!(target, "{}", class)?;
                }
            }
        }
    }

    Ok(())
}
