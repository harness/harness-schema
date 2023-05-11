// Copyright 2021 Harness Inc. All rights reserved.
// Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
// that can be found in the licenses directory at the root of this repository, also available at
// https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

use clap::Clap;
use std::fs::{create_dir_all, remove_file, File};
use std::io::{Result, Write};
use std::path::Path;

use crate::execute::read_lines;
use crate::java_class::{MODULE_IMPORT, MODULE_IMPORT_STATIC_PATTERN, TARGET_MODULE_IMPORT, TARGET_MODULE_PATTERN};
use crate::repo::GIT_REPO_ROOT_DIR;

/// An action to be executed
#[derive(Clap)]
pub struct MoveClass {
    #[clap(long)]
    from_module: String,

    #[clap(long)]
    from_location: String,

    #[clap(long)]
    to_module: String,
}

pub fn move_class(opts: MoveClass) {
    let source_file = &format!(
        "{}/{}/{}",
        GIT_REPO_ROOT_DIR.as_str(),
        opts.from_module,
        opts.from_location
    );
    let target_file = &format!(
        "{}/{}/{}",
        GIT_REPO_ROOT_DIR.as_str(),
        opts.to_module,
        opts.from_location
    );

    println!("Moving class from {} to {}", source_file, target_file);

    match copy_class(source_file, target_file) {
        Ok(_) => {
            remove_file(&source_file).unwrap();
        }
        Err(err) => {
            println!("Failed to move the class with error: {}", err);
            remove_file(&target_file).unwrap();
        }
    }
}

fn copy_class(source_file: &String, target_file: &String) -> Result<()> {
    let lines = read_lines(source_file)?;

    let target_dir = Path::new(target_file).parent().unwrap();
    create_dir_all(target_dir)?;

    let mut target = File::create(target_file)?;

    for line in lines {
        let l = line?;
        if MODULE_IMPORT.eq(&l) || TARGET_MODULE_IMPORT.eq(&l) {
            continue;
        }

        let step1_line = TARGET_MODULE_PATTERN.replace(&l, "");
        let final_line = MODULE_IMPORT_STATIC_PATTERN.replace(&step1_line, "");

        if final_line.is_empty() && !l.is_empty() {
            continue;
        }

        writeln!(target, "{}", &final_line)?;
    }

    target.flush()?;

    Ok(())
}
