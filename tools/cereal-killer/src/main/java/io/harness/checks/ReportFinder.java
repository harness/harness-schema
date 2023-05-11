/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ReportFinder {
  private final String baseDir;

  public ReportFinder(String baseDir) {
    this.baseDir = baseDir;
  }

  public List<String> findSurefireReports() throws IOException {
    List<String> surefireReports = new ArrayList<>();
    PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/target/surefire-reports/TEST-*.xml");
    Files.walkFileTree(Paths.get(baseDir), new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        if (pathMatcher.matches(path)) {
          surefireReports.add(path.toString());
        }
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path path, IOException exc) {
        return FileVisitResult.CONTINUE;
      }
    });
    return surefireReports;
  }
}
