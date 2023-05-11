// Copyright 2021 Harness Inc. All rights reserved.
// Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
// that can be found in the licenses directory at the root of this repository, also available at
// https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

use clap::Clap;
use enumset::{EnumSet, EnumSetType};
use multimap::MultiMap;
use rayon::iter::IntoParallelRefIterator;
use rayon::iter::ParallelIterator;
use std::cmp::Ordering;
use std::cmp::Ordering::Equal;
use std::collections::{HashMap, HashSet};
use std::process::exit;
use strum::IntoEnumIterator;
use strum_macros::EnumIter;
use strum_macros::EnumString;

use crate::java_class::{JavaClass, JavaClassTraits, UNKNOWN_LOCATION};
use crate::java_module::{modules, JavaModule, JavaModuleTraits};
use crate::team::UNKNOWN_TEAM;

const DEPENDENCY_WARNING_DEPTH: usize = 12;
const CIRCULAR_DEPENDENCY_WARNING_DEPTH: usize = 5;

#[derive(PartialEq, Eq, Debug, Copy, Clone, EnumIter, EnumString)]
enum Kind {
    Critical,
    Error,
    Warning,
    AutoAction,
    DevAction,
    Blocked,
    ToDo,
}

static WEIGHTS: [i32; 7] = [5, 3, 2, 1, 2, 1, 1];

#[derive(Debug, EnumSetType)]
enum Explanation {
    Empty,
    TeamIsMissing,
    ClassAlreadyInTheTargetModule,
    DeprecatedModule,
    UsedInDeprecatedClass,
    BreakDependencyOnModule,
    CircularDependencyWithMultipleTargets,
}

pub const EXPLANATION_TEAM_IS_MISSING: &str =
    "Please use the source level java annotation io.harness.annotations.dev.OwnedBy
to specify which team is the owner of the class. The list of teams is in the
enum io.harness.annotations.dev.HarnessTeam.";

pub const EXPLANATION_CLASS_IS_ALREADY_IN_THE_TARGET_MODULE: &str =
    "When class has a target module that is consistent with the module that it is
already in, this could mean one of these two:
    1. The annotation was left during the promotion/demotion - please remove it
    2. The target module is wrong - please correct the module";

pub const EXPLANATION_CLASS_IN_DEPRECATED_MODULE: &str =
    "When a module is deprecated, every class that is still in use should be
moved to another module. To plan it better we start with annotating the class
with a source level annotation io.harness.annotations.dev.TargetModule. This
will allow aeriform to scan for dependencies and point out issues with such
promotion/demotion without the need to discover them the hard way. The list
of the available modules is in io.harness.annotations.dev.HarnessModule.
WARNING: Add target modules with cation. If wrong targets are specified
         this could lead to all sorts of inappropriate error reports.";

pub const EXPLANATION_CLASS_USED_DEPRECATED_CLASS: &str =
    "When a class is deprecated, every class that it depends on should be
removed. Obviously removing the deprecated class all together will eliminate this
issue. In the spirit of allowing for iterative progress though, we report that
need independently. This is especially useful when big registration classes are
deprecated and removing dependencies class by class make more sense.";

pub const EXPLANATION_TOO_MANY_ISSUE_POINTS_PER_CLASS: &str =
    "Please resolve the necessary issues so your issue points drop under the expected limit.
Note resolving some of the issues with higher issue points weight might be harder,
but you will need to resolve less of those.";

pub const EXPLANATION_DEPENDENCY_TO_CLASS_IN_BREAK_DEPENDENCY_ON_MODULE: &str =
    "When class from a module that needs to break dependency from another module,
depends on class from such module, this dependency needs to be broken.
This can be done with breaking the dependency of the two classes or moving the
classes accordingly, so they do not belong to module that should not depend to each other.";

pub const EXPLANATION_CIRCULAR_DEPENDENCY_WITH_MULTIPLE_TARGETS: &str =
    "When a class is circularly dependent to itself which is allowed in a
java module, this means that all these classes need to be mainteined in a
single module. Obviously, if we have classes in the circle that point to
different targets we have a problem. This could be resolved with changing the
target modules for the classes to be the same or with breaking some of the
dependencies in the chain.";

/// A sub-command to analyze the project module targets and dependencies
#[derive(Clap)]
pub struct Analyze {
    /// Return error exit code if issues are found.
    #[clap(short, long)]
    exit_code: bool,

    /// Filter the reports by affected class class_filter.
    #[clap(short, long)]
    class_filter: Vec<String>,

    /// Filter the reports by affected class specified by its location class_filter.
    #[clap(short, long)]
    location_class_filter: Vec<String>,

    /// Filter the reports by affected module module_filter.
    #[clap(short, long)]
    module_filter: Option<String>,

    /// Filter the reports by affected module root root_filter.
    #[clap(short, long)]
    root_filter: Vec<String>,

    /// Filter the reports by team.
    #[clap(short, long)]
    team_filter: Option<String>,

    /// Filter the reports by team.
    #[clap(short, long)]
    only_team_filter: bool,

    #[clap(short, long)]
    kind_filter: Vec<Kind>,

    #[clap(short, long)]
    auto_actionable_filter: bool,

    #[clap(long)]
    auto_actionable_command: bool,

    #[clap(long)]
    issue_points_per_class_limit: Option<f64>,

    #[clap(short, long)]
    indirect: Option<u32>,

    #[clap(long)]
    top_blockers: Option<u32>,
}

#[derive(Debug)]
struct Report {
    kind: Kind,
    explanation: Explanation,
    message: String,
    action: String,

    for_class: String,
    for_team: String,
    indirect_classes: HashSet<String>,

    for_modules: HashSet<String>,
}

pub fn analyze(opts: Analyze) {
    println!("loading...");

    let modules = modules();
    // println!("{:?}", modules);

    if opts.module_filter.is_some()
        && !modules
            .keys()
            .any(|module_name| module_name.eq(opts.module_filter.as_ref().unwrap()))
    {
        panic!("There is no module {}", opts.module_filter.unwrap());
    }

    let mut class_modules: HashMap<&JavaClass, &JavaModule> = HashMap::new();

    let mut class_dependees: MultiMap<&String, &String> = MultiMap::new();

    modules.values().for_each(|module| {
        module.srcs.iter().for_each(|src| {
            // println!("{:?}", src.0);
            match class_modules.get(src.1) {
                None => {
                    class_modules.insert(src.1, module);
                }
                Some(&current) => {
                    if current.index < module.index {
                        class_modules.insert(src.1, module);
                    }
                }
            }

            src.1
                .dependencies
                .iter()
                .for_each(|dependee| class_dependees.insert(dependee, src.0))
        });
    });

    let classes = class_modules
        .keys()
        .map(|&class| (class.name.clone(), class))
        .collect::<HashMap<String, &JavaClass>>();

    for class in opts.class_filter.iter() {
        if !classes.contains_key(class) {
            panic!("There is no class {}", class);
        }
    }

    let class_locations = class_modules
        .keys()
        .map(|&class| (class.location.clone(), class))
        .collect::<HashMap<String, &JavaClass>>();

    for class_location in opts.location_class_filter.iter() {
        if !class_locations.contains_key(class_location) {
            panic!("There is no class with location {}", class_location);
        }
    }

    if opts.module_filter.is_some() {
        println!("analyzing for module {} ...", opts.module_filter.as_ref().unwrap());
    } else if !opts.root_filter.is_empty() {
        println!("analyzing for root {:?} ...", opts.root_filter);
    } else {
        println!("analyzing...");
    }

    let mut results: Vec<Report> = Vec::new();
    modules.iter().for_each(|tuple| {
        results.extend(check_for_classes_in_more_than_one_module(
            tuple.1,
            &class_modules,
            &modules,
        ));
        results.extend(check_for_reversed_dependency(tuple.1, &modules));
    });

    results.extend(
        class_modules
            .par_iter()
            .map(|tuple| {
                let mut class_results: Vec<Report> = Vec::new();
                class_results.extend(check_for_circular_dependency(tuple.0, &classes, &class_modules));
                class_results.extend(check_for_package(
                    tuple.0,
                    tuple.1,
                    &tuple.0.target_module_team(&modules),
                ));
                class_results.extend(check_for_team(tuple.0, tuple.1, &tuple.0.target_module_team(&modules)));
                class_results.extend(check_already_in_target(
                    tuple.0,
                    tuple.1,
                    &tuple.0.target_module_team(&modules),
                ));
                class_results.extend(check_for_extra_break(
                    tuple.0,
                    tuple.1,
                    &tuple.0.target_module_team(&modules),
                ));
                class_results.extend(check_for_module_that_need_to_break_dependency_on(
                    tuple.0,
                    tuple.1,
                    &modules,
                    &classes,
                    &class_modules,
                ));
                class_results.extend(check_for_promotion(
                    tuple.0,
                    tuple.1,
                    &modules,
                    &classes,
                    &class_modules,
                ));
                class_results.extend(check_for_demotion(
                    tuple.0,
                    class_dependees.get_vec(&tuple.0.name),
                    tuple.1,
                    &modules,
                    &classes,
                    &class_modules,
                ));
                class_results.extend(check_for_deprecated_module(
                    tuple.0,
                    tuple.1,
                    &tuple.0.target_module_team(&modules),
                ));
                class_results.extend(check_for_deprecation(
                    tuple.0,
                    class_dependees.get_vec(&tuple.0.name),
                    tuple.1,
                    &modules,
                    &classes,
                    &class_modules,
                ));

                class_results
            })
            .flatten()
            .collect::<Vec<Report>>(),
    );

    let mut kind_summary: HashMap<usize, i32> = HashMap::new();
    let mut team_summary: HashMap<&String, HashMap<usize, i32>> = HashMap::new();

    results.sort_by(|a, b| cmp_report(a, b));

    results.dedup_by(|a, b| cmp_report(a, b) == Equal);

    println!("Detecting indirectly involved classes...");

    let indirect_classes: &mut HashSet<&String> = &mut HashSet::new();
    for _ in 0..opts.indirect.unwrap_or(0) {
        let original: HashSet<&String> = indirect_classes.iter().map(|&s| s).collect();

        results
            .iter()
            .filter(|&report| filter_report(&opts, report, &class_locations) || original.contains(&report.for_class))
            .for_each(|report| {
                indirect_classes.extend(&report.indirect_classes);
            });

        if original.len() == indirect_classes.len() {
            break;
        }
    }

    let mut explanations: EnumSet<Explanation> = EnumSet::empty();
    let mut total_count = 0;

    let filtered: Vec<&Report> = results
        .iter()
        .filter(|&report| {
            filter_report(&opts, report, &class_locations) || indirect_classes.contains(&report.for_class)
        })
        .filter(|&report| filter_by_kind(&opts, report))
        .filter(|&report| filter_by_auto_actionable(&opts, report))
        .filter(|&report| filter_by_team(&opts, report))
        .collect();

    filtered.iter().for_each(|&report| {
        println!("{:?}: [{}] {}", &report.kind, &report.for_team, &report.message);
        if opts.auto_actionable_command && !report.action.is_empty() {
            println!("   {}", &report.action);
        }
        total_count += 1;
        *kind_summary.entry(report.kind as usize).or_insert(0) += 1;
        let ts = team_summary.entry(&report.for_team).or_insert(HashMap::new());
        *ts.entry(report.kind as usize).or_insert(0) += 1;
        explanations.insert(report.explanation);
    });

    if opts.top_blockers.unwrap_or(0) > 0 {
        println!();
        report_blockers(opts.top_blockers.unwrap(), &filtered);
    }

    println!();

    if total_count == 0 {
        println!("aeriform did not find any issues");
        exit(0);
    }

    team_summary.iter().for_each(|tuple| {
        report(&Some(tuple.0.to_string()), tuple.1, &modules, &class_modules);
        ()
    });

    let ipc = report(&None, &kind_summary, &modules, &class_modules);

    if explanations.contains(Explanation::TeamIsMissing) {
        println!();
        println!("{}", EXPLANATION_TEAM_IS_MISSING);
    }
    if explanations.contains(Explanation::ClassAlreadyInTheTargetModule) {
        println!();
        println!("{}", EXPLANATION_CLASS_IS_ALREADY_IN_THE_TARGET_MODULE);
    }
    if explanations.contains(Explanation::DeprecatedModule) {
        println!();
        println!("{}", EXPLANATION_CLASS_IN_DEPRECATED_MODULE);
    }
    if explanations.contains(Explanation::UsedInDeprecatedClass) {
        println!();
        println!("{}", EXPLANATION_CLASS_USED_DEPRECATED_CLASS);
    }

    if explanations.contains(Explanation::BreakDependencyOnModule) {
        println!();
        println!("{}", EXPLANATION_DEPENDENCY_TO_CLASS_IN_BREAK_DEPENDENCY_ON_MODULE);
    }
    if explanations.contains(Explanation::CircularDependencyWithMultipleTargets) {
        println!();
        println!("{}", EXPLANATION_CIRCULAR_DEPENDENCY_WITH_MULTIPLE_TARGETS);
    }

    if opts.issue_points_per_class_limit.is_some() && opts.issue_points_per_class_limit.unwrap() < ipc {
        println!();
        println!(
            "The analyze found {} that is more than the expected limit of issues per class {}.",
            ipc,
            opts.issue_points_per_class_limit.unwrap()
        );
        println!("{}", EXPLANATION_TOO_MANY_ISSUE_POINTS_PER_CLASS);
        exit(1);
    }

    if opts.exit_code {
        exit(1);
    }
}

fn cmp_report(a: &Report, b: &Report) -> Ordering {
    let ordering = (a.kind as usize).cmp(&(b.kind as usize));
    if ordering != Equal {
        ordering
    } else {
        let ordering = a.message.cmp(&b.message);
        if ordering != Equal {
            ordering
        } else {
            a.for_team.cmp(&b.for_team)
        }
    }
}

pub struct Blocker {
    class: String,
    operations: usize,
}

fn report_blockers(top: u32, results: &Vec<&Report>) {
    let mut blockers_map: HashMap<&String, HashSet<&String>> = HashMap::new();

    results.iter().for_each(|result| {
        result.indirect_classes.iter().for_each(|blocker| {
            let blockers = blockers_map.entry(blocker).or_insert(HashSet::new());
            blockers.insert(&result.for_class);
        })
    });

    let mut unstable: HashSet<&String> = blockers_map.keys().map(|&class| class).collect();

    while unstable.len() > 0 {
        let extended: HashMap<&String, HashSet<&String>> = blockers_map
            .par_iter()
            .map(|(&class, blocked)| {
                let mut extended = HashSet::new();
                extended.extend(blocked);

                let x: HashSet<&String> = blocked
                    .par_iter()
                    .map(|class| blockers_map.get(class))
                    .filter(|option| option.is_some())
                    .map(|option| option.unwrap())
                    .flatten()
                    .map(|&class| class)
                    .collect();

                extended.extend(x);

                (extended.len() == blocked.len(), class, extended)
            })
            .filter(|(stable, _, _)| !stable)
            .map(|(_, class, extended)| (class, extended))
            .collect();

        unstable = extended.keys().map(|&class| class).collect();
        blockers_map.extend(extended);
    }

    let mut blockers: Vec<Blocker> = blockers_map
        .iter()
        .map(|(&class, blocked)| Blocker {
            class: class.clone(),
            operations: blocked.len(),
        })
        .collect();

    blockers.sort_by(|a, b| b.operations.cmp(&a.operations));

    blockers
        .iter()
        .take(top as usize)
        .for_each(|blocker| println!("Class {} blocks {} operations", blocker.class, blocker.operations))
}

fn report(
    team: &Option<String>,
    summary: &HashMap<usize, i32>,
    modules: &HashMap<String, JavaModule>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> f64 {
    println!();
    if team.is_some() {
        println!("Report for team {}", team.as_ref().unwrap())
    }

    let mut issue_points = 0;
    for kind in Kind::iter() {
        let count = summary.get(&(kind as usize));

        if count.is_some() {
            let ip = count.unwrap() * WEIGHTS[kind as usize];
            issue_points += ip;

            println!("{:?} -> {} * {} = {}", kind, count.unwrap(), WEIGHTS[kind as usize], ip);
        }
    }

    let count = class_modules
        .iter()
        .filter(|(&class, &module)| {
            team.is_none()
                || class
                    .team(module, &class.target_module_team(&modules))
                    .eq(team.as_ref().unwrap())
        })
        .count();

    let ipc = issue_points as f64 / count as f64;
    println!("IssuePoints -> {} / {} classes = {}", issue_points, count, ipc);

    ipc
}

fn filter_report(opts: &Analyze, report: &Report, class_locations: &HashMap<String, &JavaClass>) -> bool {
    filter_by_class(&opts, report, class_locations) && filter_by_module(&opts, report) && filter_by_root(&opts, report)
}

fn filter_by_class(opts: &Analyze, report: &Report, class_locations: &HashMap<String, &JavaClass>) -> bool {
    (opts.class_filter.is_empty() && opts.location_class_filter.is_empty())
        || opts.class_filter.iter().any(|class| report.for_class.eq(class))
        || opts.location_class_filter.iter().any(|class_location| {
            let class = &class_locations.get(class_location).unwrap().name;
            report.for_class.eq(class)
        })
}

fn filter_by_module(opts: &Analyze, report: &Report) -> bool {
    opts.module_filter.is_none() || report.for_modules.contains(opts.module_filter.as_ref().unwrap())
}

fn filter_by_kind(opts: &Analyze, report: &Report) -> bool {
    opts.kind_filter.is_empty() || opts.kind_filter.iter().any(|&kind| kind == report.kind)
}

fn filter_by_team(opts: &Analyze, report: &Report) -> bool {
    opts.team_filter.is_none()
        || (report.for_team.eq(opts.team_filter.as_ref().unwrap())
            || (!opts.only_team_filter && report.for_team.eq(UNKNOWN_TEAM)))
}

fn filter_by_auto_actionable(opts: &Analyze, report: &Report) -> bool {
    !opts.auto_actionable_filter || !report.action.is_empty()
}

fn filter_by_root(opts: &Analyze, report: &Report) -> bool {
    opts.root_filter.is_empty() || report.for_modules.iter().any(|name| is_with_root(opts, name))
}

fn is_with_root(opts: &Analyze, module_name: &String) -> bool {
    opts.root_filter
        .iter()
        .any(|root| module_name.starts_with(root) && module_name.chars().nth(root.len()).unwrap() == ':')
}

fn check_for_extra_break(class: &JavaClass, module: &JavaModule, target_module_team: &Option<String>) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    class
        .break_dependencies_on
        .iter()
        .filter(|&break_dependency| !class.dependencies.contains(break_dependency))
        .for_each(|break_dependency| {
            let mut modules: HashSet<String> = HashSet::new();
            modules.insert(module.name.clone());
            if class.target_module.is_some() {
                modules.insert(class.target_module.as_ref().unwrap().clone());
            }

            results.push(Report {
                kind: Kind::Critical,
                explanation: Explanation::Empty,
                message: format!("{} has no dependency on {}", class.name, break_dependency),
                action: Default::default(),
                for_class: class.name.clone(),
                for_team: class.team(module, target_module_team),
                indirect_classes: Default::default(),
                for_modules: modules,
            })
        });

    results
}

fn check_for_classes_in_more_than_one_module(
    module: &JavaModule,
    classes: &HashMap<&JavaClass, &JavaModule>,
    modules: &HashMap<String, JavaModule>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    module
        .srcs
        .values()
        .filter(|class| class.location.ne("n/a"))
        .for_each(|class| {
            let tracked_module = classes.get(class).unwrap();
            if tracked_module.name.ne(&module.name) {
                results.push(Report {
                    kind: Kind::Critical,
                    explanation: Explanation::Empty,
                    message: format!("{} appears in {} and {}", class.name, module.name, tracked_module.name),
                    action: Default::default(),
                    for_class: class.name.clone(),
                    for_team: class.team(module, &class.target_module_team(modules)),
                    indirect_classes: Default::default(),
                    for_modules: [module.name.clone(), tracked_module.name.clone()]
                        .iter()
                        .cloned()
                        .collect(),
                });
            }
        });

    results
}

fn check_for_reversed_dependency(module: &JavaModule, modules: &HashMap<String, JavaModule>) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    module.dependencies.iter().for_each(|name| {
        let dependent = modules
            .get(name)
            .expect(&format!("Dependent module {} does not exists", name));

        if module.index >= dependent.index {
            results.push(Report {
                kind: Kind::Critical,
                explanation: Explanation::Empty,
                message: format!(
                    "Module {} depends on module {} that is not lower",
                    module.name, dependent.name
                ),
                action: Default::default(),
                for_class: Default::default(),
                for_team: module.team(),
                indirect_classes: Default::default(),
                for_modules: [module.name.clone(), dependent.name.clone()].iter().cloned().collect(),
            });
        }
    });

    results
}

fn check_already_in_target(class: &JavaClass, module: &JavaModule, target_module_team: &Option<String>) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    let target_module = class.target_module.as_ref();
    if target_module.is_none() {
        results
    } else {
        if module.name.eq(target_module.unwrap()) {
            results.push(Report {
                kind: Kind::Critical,
                explanation: Explanation::ClassAlreadyInTheTargetModule,
                message: format!("{} target module is the same as the module of the class", class.name),
                action: Default::default(),
                for_class: class.name.clone(),
                for_team: class.team(module, target_module_team),
                indirect_classes: Default::default(),
                for_modules: [module.name.clone()].iter().cloned().collect(),
            })
        }

        results
    }
}

fn check_for_module_that_need_to_break_dependency_on(
    class: &JavaClass,
    module: &JavaModule,
    modules: &HashMap<String, JavaModule>,
    classes: &HashMap<String, &JavaClass>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    let target_module_name = class.target_module.as_ref();
    if target_module_name.is_some() {
        return results;
    }

    class.dependencies.iter().for_each(|src| {
        let &dependent_class = classes.get(src).expect(&format!(
            "Class {} depends on source {} that not have any module",
            class.name, src
        ));

        if dependent_class.deprecated {
            return ();
        }

        let &dependent_real_module = class_modules.get(dependent_class).expect(&format!(
            "The class {} is not find in the modules",
            dependent_class.name
        ));

        let dependent_target_module = if dependent_class.target_module.is_some() {
            let dependent_target_module = modules.get(dependent_class.target_module.as_ref().unwrap());
            if dependent_target_module.is_none() {
                results.push(target_module_needed(dependent_class));
                return ();
            }

            dependent_target_module.unwrap()
        } else {
            dependent_real_module
        };

        if module.break_dependencies_on.contains(&dependent_target_module.name) {
            results.push(Report {
                kind: Kind::Error,
                explanation: Explanation::BreakDependencyOnModule,
                message: format!(
                    "{} depends on {} that is in module {} but {} should break dependency on it",
                    class.name, dependent_class.name, dependent_target_module.name, module.name
                ),
                action: Default::default(),
                for_class: class.name.clone(),
                for_team: class.team(module, &None),
                indirect_classes: [dependent_class.name.clone()].iter().cloned().collect(),
                for_modules: [module.name.clone(), dependent_target_module.name.clone()]
                    .iter()
                    .cloned()
                    .collect(),
            });
        }
    });

    results
}

fn check_for_promotion(
    class: &JavaClass,
    module: &JavaModule,
    modules: &HashMap<String, JavaModule>,
    classes: &HashMap<String, &JavaClass>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    let target_module_name = class.target_module.as_ref();
    if target_module_name.is_none() {
        return results;
    }
    let target_module_option = modules.get(target_module_name.unwrap());
    if target_module_option.is_none() {
        results.push(target_module_needed(class));
        return results;
    }

    let target_module = target_module_option.unwrap();

    if module.index > target_module.index {
        return results;
    }

    let mut issue = false;
    let mut all_classes: HashSet<String> = HashSet::new();
    let mut not_ready_yet = Vec::new();
    class.dependencies.iter().for_each(|src| {
        let &dependent_class = classes
            .get(src)
            .expect(&format!("The source {} is not find in any module", src));

        let &dependent_real_module = class_modules.get(dependent_class).expect(&format!(
            "The class {} is not find in the modules",
            dependent_class.name
        ));

        let dependent_target_module = if dependent_class.target_module.is_some() {
            let dependent_target_module = modules.get(dependent_class.target_module.as_ref().unwrap());
            if dependent_target_module.is_none() {
                issue = true;
                results.push(target_module_needed(dependent_class));
                return ();
            }

            dependent_target_module.unwrap()
        } else {
            dependent_real_module
        };

        if !target_module.name.eq(&dependent_target_module.name)
            && !target_module.dependencies.contains(&dependent_target_module.name)
        {
            issue = true;

            if dependent_class.deprecated {
                return ();
            }

            let mdls = [
                module.name.clone(),
                dependent_target_module.name.clone(),
                target_module.name.clone(),
            ]
            .iter()
            .cloned()
            .collect();

            if class.break_dependencies_on.contains(src) {
                results.push(Report {
                    kind: Kind::DevAction,
                    explanation: Explanation::Empty,
                    message: format!(
                        "{} depends on {} and this dependency has to be broken",
                        class.name, dependent_class.name
                    ),
                    action: Default::default(),
                    for_class: class.name.clone(),
                    for_team: class.team(module, &target_module.team),
                    indirect_classes: Default::default(),
                    for_modules: mdls,
                });
            } else if !dependent_real_module.external() {
                let msg = if dependent_target_module.index > target_module.index {
                    format!(
                        "{} depends on {} that is in module {} but {} does not depend on it",
                        class.name, dependent_class.name, dependent_target_module.name, target_module.name
                    )
                } else {
                    format!(
                        "{} depends on {} that is in module {} but {} cannot depend on it",
                        class.name, dependent_class.name, dependent_target_module.name, target_module.name
                    )
                };

                results.push(Report {
                    kind: Kind::Error,
                    explanation: Explanation::Empty,
                    message: msg,
                    action: Default::default(),
                    for_class: class.name.clone(),
                    for_team: class.team(module, &target_module.team),
                    indirect_classes: [dependent_class.name.clone()].iter().cloned().collect(),
                    for_modules: mdls,
                });
            }
        }

        if dependent_real_module.index < target_module.index {
            all_classes.insert(src.clone());
            not_ready_yet.push(format!("{} to {}", src, dependent_target_module.name));
        }
    });

    if !issue && not_ready_yet.is_empty() {
        let mdls = [module.name.clone(), target_module.name.clone()]
            .iter()
            .cloned()
            .collect();
        let module = class_modules.get(class);

        let msg = format!("{} is ready to go to {}", class.name, target_module.name);

        results.push(match module {
            None => Report {
                kind: Kind::DevAction,
                explanation: Explanation::Empty,
                action: Default::default(),
                message: msg,
                for_class: class.name.clone(),
                for_team: class.simple_team(),
                indirect_classes: Default::default(),
                for_modules: mdls,
            },
            Some(&module) => Report {
                kind: Kind::AutoAction,
                explanation: Explanation::Empty,
                message: msg,
                action: format!(
                    "execute move-class --from-module=\"{}\" --from-location=\"{}\" --to-module=\"{}\"",
                    module.directory,
                    class.relative_location(),
                    target_module.directory
                ),
                for_class: class.name.clone(),
                for_team: class.team(module, &target_module.team),
                indirect_classes: Default::default(),
                for_modules: mdls,
            },
        });
    }

    if !issue && !not_ready_yet.is_empty() {
        all_classes.insert(class.name.clone());

        not_ready_yet.sort_by(|a, b| a.cmp(b));

        results.push(Report {
            kind: Kind::Blocked,
            explanation: Explanation::Empty,
            message: format!(
                "{} does not have untargeted dependencies to go to {}. First move {}",
                class.name,
                target_module.name,
                not_ready_yet.join(", ")
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.team(module, &target_module.team),
            indirect_classes: all_classes,
            for_modules: [module.name.clone(), target_module.name.clone()]
                .iter()
                .cloned()
                .collect(),
        });
    }

    results
}

fn check_for_demotion(
    class: &JavaClass,
    dependees: Option<&Vec<&String>>,
    module: &JavaModule,
    modules: &HashMap<String, JavaModule>,
    classes: &HashMap<String, &JavaClass>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    if class.deprecated {
        return results;
    }

    let target_module_name = class.target_module.as_ref();
    if target_module_name.is_none() {
        return results;
    }
    let target_module_option = modules.get(target_module_name.unwrap());
    if target_module_option.is_none() {
        results.push(target_module_needed(class));
        return results;
    }

    let target_module = target_module_option.unwrap();

    if module.index < target_module.index {
        return results;
    }

    let mut issue = false;
    let mut all_classes: HashSet<String> = HashSet::new();
    let mut not_ready_yet = Vec::new();
    if dependees.is_some() {
        dependees.unwrap().iter().for_each(|&dependee| {
            let &dependee_class = classes
                .get(dependee)
                .expect(&format!("The source {} is not find in any module", dependee));

            if dependee_class.deprecated {
                issue = true;

                let indirect_classes = [dependee_class.name.clone()].iter().cloned().collect();

                results.push(Report {
                    kind: Kind::DevAction,
                    explanation: Explanation::UsedInDeprecatedClass,
                    message: format!(
                        "{} is deprecated and depends on {}, this dependency has to be broken",
                        dependee_class.name, class.name,
                    ),
                    action: Default::default(),
                    for_class: class.name.clone(),
                    for_team: class.team(module, &class.target_module_team(modules)),
                    indirect_classes: indirect_classes,
                    for_modules: [module.name.clone()].iter().cloned().collect(),
                });
                return ();
            }

            let &dependee_real_module = class_modules
                .get(dependee_class)
                .expect(&format!("The class {} is not find in the modules", dependee_class.name));

            let dependee_target_module = if dependee_class.target_module.is_some() {
                let dependee_target_module = modules.get(dependee_class.target_module.as_ref().unwrap());
                if dependee_target_module.is_none() {
                    issue = true;
                    results.push(target_module_needed(dependee_class));
                    return ();
                }

                dependee_target_module.unwrap()
            } else {
                dependee_real_module
            };

            if !dependee_target_module.name.eq(&target_module.name)
                && !dependee_target_module.dependencies.contains(&target_module.name)
            {
                issue = true;
                let mdls = [
                    module.name.clone(),
                    dependee_target_module.name.clone(),
                    target_module.name.clone(),
                ]
                .iter()
                .cloned()
                .collect();
                let indirect_classes = [dependee_class.name.clone()].iter().cloned().collect();
                if dependee_class.break_dependencies_on.contains(&class.name) {
                    results.push(Report {
                        kind: Kind::DevAction,
                        explanation: Explanation::Empty,
                        message: format!(
                            "{} depends on {} and this dependency has to be broken",
                            dependee_class.name, class.name,
                        ),
                        action: Default::default(),
                        for_class: dependee_class.name.clone(),
                        for_team: dependee_class.team(module, &dependee_class.target_module_team(modules)),
                        indirect_classes: indirect_classes,
                        for_modules: mdls,
                    });
                } else {
                    let msg = if target_module.index > dependee_target_module.index {
                        format!(
                            "{} depends on {} that is in module {} but {} does not depend on it",
                            dependee_class.name, class.name, target_module.name, dependee_target_module.name
                        )
                    } else {
                        format!(
                            "{} depends on {} that is in module {} but {} cannot depend on it",
                            dependee_class.name, class.name, target_module.name, dependee_target_module.name
                        )
                    };

                    results.push(Report {
                        kind: Kind::Error,
                        explanation: Explanation::Empty,
                        message: msg,
                        action: Default::default(),
                        for_team: class.team(module, &target_module.team),
                        for_class: class.name.clone(),
                        indirect_classes: indirect_classes,
                        for_modules: mdls,
                    });
                }
            }

            if dependee_real_module.index > target_module.index {
                all_classes.insert(dependee.clone());
                not_ready_yet.push(format!("{} to {}", dependee, dependee_target_module.name));
            }
        });
    }

    if !issue && not_ready_yet.is_empty() {
        let mdls = [module.name.clone(), target_module.name.clone()]
            .iter()
            .cloned()
            .collect();

        let module = class_modules.get(class);

        let msg = format!("{} is ready to go to {}", class.name, target_module.name);

        results.push(match module {
            None => Report {
                kind: Kind::DevAction,
                explanation: Explanation::Empty,
                action: Default::default(),
                message: msg,
                for_class: class.name.clone(),
                for_team: class.simple_team(),
                indirect_classes: Default::default(),
                for_modules: mdls,
            },
            Some(&module) => Report {
                kind: Kind::AutoAction,
                explanation: Explanation::Empty,
                message: msg,
                action: format!(
                    "execute move-class --from-module=\"{}\" --from-location=\"{}\" --to-module=\"{}\"",
                    module.directory,
                    class.relative_location(),
                    target_module.directory
                ),
                for_class: class.name.clone(),
                for_team: class.team(module, &target_module.team),
                indirect_classes: Default::default(),
                for_modules: mdls,
            },
        });
    }

    if !issue && !not_ready_yet.is_empty() {
        all_classes.insert(class.name.clone());

        not_ready_yet.sort_by(|a, b| a.cmp(b));

        results.push(Report {
            kind: Kind::Blocked,
            explanation: Explanation::Empty,
            message: format!(
                "{} does not have dependees to go to {}. First move {}",
                class.name,
                target_module.name,
                not_ready_yet.join(", ")
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.team(module, &target_module.team),
            indirect_classes: all_classes,
            for_modules: [module.name.clone(), target_module.name.clone()]
                .iter()
                .cloned()
                .collect(),
        });
    }

    results
}

fn target_module_needed(class: &JavaClass) -> Report {
    Report {
        kind: Kind::DevAction,
        explanation: Explanation::Empty,
        message: format!(
            "Target module {} needs to be created.",
            class.target_module.as_ref().unwrap()
        ),
        action: Default::default(),
        for_class: class.name.clone(),
        for_team: class.simple_team(),
        indirect_classes: Default::default(),
        for_modules: Default::default(),
    }
}

fn check_for_circular_dependency(
    class: &JavaClass,
    classes: &HashMap<String, &JavaClass>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> Vec<Report> {
    let module = *class_modules.get(class).expect("Class module is missing");

    let mut circle: usize = 0;
    let mut paths: Vec<(&String, usize, u32)> = Vec::new();
    let mut reached: HashSet<&String> = HashSet::new();

    paths.push((&class.name, 0, 0));

    let mut i: usize = 0;
    while i < paths.len() {
        let class_name = paths[i].0;
        let level = paths[i].2 + 1;

        i = i + 1;
        if i > 1 && class_name.eq(&class.name) {
            circle = i;
            break;
        }
        let cls_element = *classes.get(class_name).expect("Class is missing");

        cls_element
            .dependencies
            .iter()
            .filter(|&name| !cls_element.break_dependencies_on.contains(name))
            .filter(|&name| match classes.get(name) {
                None => false,
                Some(&cls) => match class_modules.get(cls) {
                    None => false,
                    Some(&dependent_module) => dependent_module.index == module.index,
                },
            })
            .for_each(|name| {
                if reached.insert(name) {
                    paths.push((name, i, level));
                }
            });
    }

    let mut results: Vec<Report> = Vec::new();

    let (max_path, max_target_modules) = calculate_path(paths.len() - 1, &paths, classes);
    if max_target_modules.len() == 1 && max_path.len() > DEPENDENCY_WARNING_DEPTH {
        results.push(Report {
            kind: Kind::Warning,
            explanation: Explanation::Empty,
            message: format!(
                "{} dependency level {} is too deep: {}",
                class.name,
                max_path.len(),
                max_path.join(", ")
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.simple_team(),
            indirect_classes: Default::default(),
            for_modules: [module.name.clone()].iter().cloned().collect(),
        });
    }

    if circle <= 1 {
        return results;
    }

    let (path, target_modules) = calculate_path(paths[circle - 1].1, &paths, classes);

    if target_modules.len() > 1 {
        results.push(Report {
            kind: Kind::Error,
            explanation: Explanation::CircularDependencyWithMultipleTargets,
            message: format!(
                "{} has circular dependency and not all classes have the same target_module: {}",
                class.name,
                path.join(", "),
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.simple_team(),
            indirect_classes: Default::default(),
            for_modules: [module.name.clone()].iter().cloned().collect(),
        });
    } else {
        if path.len() > CIRCULAR_DEPENDENCY_WARNING_DEPTH {
            results.push(Report {
                kind: Kind::Warning,
                explanation: Explanation::Empty,
                message: format!(
                    "{} has circular dependency to it-self with depth {}",
                    class.name,
                    path.len(),
                ),
                action: Default::default(),
                for_class: class.name.clone(),
                for_team: class.simple_team(),
                indirect_classes: Default::default(),
                for_modules: [module.name.clone()].iter().cloned().collect(),
            });
        }

        let target_module = target_modules.iter().next().unwrap();
        if !target_module.is_empty() {
            results.push(Report {
                kind: Kind::DevAction,
                explanation: Explanation::Empty,
                message: format!(
                    "Try to move this dependency circle to module {}: {}",
                    target_modules.iter().next().unwrap(),
                    path.join(", "),
                ),
                action: Default::default(),
                for_class: class.name.clone(),
                for_team: class.simple_team(),
                indirect_classes: Default::default(),
                for_modules: [module.name.clone(), target_module.clone()].iter().cloned().collect(),
            });
        }
    }

    return results;
}

fn calculate_path(
    start: usize,
    paths: &Vec<(&String, usize, u32)>,
    classes: &HashMap<String, &JavaClass>,
) -> (Vec<String>, HashSet<String>) {
    let mut path: Vec<String> = Vec::new();
    let mut curr = start;
    while curr != 0 {
        path.push(paths[curr - 1].0.clone());
        curr = paths[curr - 1].1;
    }

    path.reverse();

    let empty = &String::new();
    let target_modules: HashSet<String> = path
        .iter()
        .map(|name| *classes.get(name).unwrap())
        .filter(|&class| !class.deprecated)
        .map(|class| match &class.target_module {
            None => empty.clone(),
            Some(mdl) => mdl.clone(),
        })
        .collect();

    (path, target_modules)
}

fn check_for_package(class: &JavaClass, module: &JavaModule, target_module_team: &Option<String>) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    if class.package.is_some()
        && !class
            .directory_location()
            .ends_with(&class.package.as_ref().unwrap().replace(".", "/"))
    {
        results.push(Report {
            kind: Kind::Critical,
            explanation: Explanation::Empty,
            message: format!(
                "{} package does not match the location {}",
                class.package.as_ref().unwrap(),
                class.location
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.team(module, target_module_team),
            indirect_classes: Default::default(),
            for_modules: [module.name.clone()].iter().cloned().collect(),
        });
    }

    results
}

fn check_for_team(class: &JavaClass, module: &JavaModule, target_module_team: &Option<String>) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    if class.deprecated {
        return results;
    }

    if class.is_generated() {
        return results;
    }

    if UNKNOWN_LOCATION.eq(&class.location) {
        return results;
    }

    if class.name.eq("io.harness.annotations.dev.HarnessTeam") {
        return results;
    }

    if class.team(module, target_module_team).eq(UNKNOWN_TEAM) {
        results.push(Report {
            kind: Kind::ToDo,
            explanation: Explanation::TeamIsMissing,
            message: format!("{} is missing team", class.name),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: UNKNOWN_TEAM.to_string(),
            indirect_classes: Default::default(),
            for_modules: [module.name.clone()].iter().cloned().collect(),
        });
    }

    results
}

fn check_for_deprecated_module(
    class: &JavaClass,
    module: &JavaModule,
    target_module_team: &Option<String>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    if class.deprecated {
        return results;
    }

    if class.target_module.is_none() && module.deprecated {
        results.push(Report {
            kind: Kind::Warning,
            explanation: Explanation::DeprecatedModule,
            message: format!(
                "{} is in deprecated module {} and has no target module",
                class.name, module.name
            ),
            action: Default::default(),
            for_class: class.name.clone(),
            for_team: class.team(module, target_module_team),
            indirect_classes: Default::default(),
            for_modules: [module.name.clone()].iter().cloned().collect(),
        });
    }

    results
}

fn check_for_deprecation(
    class: &JavaClass,
    dependees: Option<&Vec<&String>>,
    module: &JavaModule,
    modules: &HashMap<String, JavaModule>,
    classes: &HashMap<String, &JavaClass>,
    class_modules: &HashMap<&JavaClass, &JavaModule>,
) -> Vec<Report> {
    let mut results: Vec<Report> = Vec::new();

    if !class.deprecated {
        return results;
    }
    results.push(Report {
        kind: match dependees {
            None => Kind::AutoAction,
            Some(_) => Kind::Blocked,
        },
        explanation: Explanation::Empty,
        message: format!("{} is deprecated, remove it", class.name),
        // TODO: add action for this
        action: Default::default(),
        for_class: class.name.clone(),
        for_team: class.team(module, &class.target_module_team(modules)),
        indirect_classes: Default::default(),
        for_modules: [module.name.clone()].iter().cloned().collect(),
    });

    if dependees.is_some() {
        dependees.unwrap().iter().for_each(|&dependee| {
            let dependent_class = classes.get(dependee).expect(&format!("unknown class {}", dependee));
            let dependent_module = *class_modules.get(dependent_class).unwrap();
            results.push(Report {
                kind: Kind::DevAction,
                explanation: Explanation::Empty,
                message: format!(
                    "{} break dependency on deprecated class {}",
                    dependent_class.name, class.name
                ),
                action: Default::default(),
                for_class: dependent_class.name.clone(),
                for_team: dependent_class.team(dependent_module, &dependent_class.target_module_team(modules)),
                indirect_classes: Default::default(),
                for_modules: [dependent_module.name.clone()].iter().cloned().collect(),
            });
        });
    }

    results
}
