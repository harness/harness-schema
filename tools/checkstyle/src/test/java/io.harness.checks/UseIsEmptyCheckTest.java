/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.TreeWalker;
import org.junit.Test;

public class UseIsEmptyCheckTest extends AbstractModuleTestSupport {
  @Override
  protected String getPackageLocation() {
    return "io.harness.checks";
  }

  public DefaultConfiguration config() {
    DefaultConfiguration config = createModuleConfig(UseIsEmptyCheck.class);

    DefaultConfiguration twConf = createModuleConfig(TreeWalker.class);
    twConf.addChild(config);
    twConf.addAttribute("fileExtensions", "jv");

    return twConf;
  }

  @Test
  public void testNullOrIsEmptyDetectIssues() throws Exception {
    final String[] expected = {"7:22: Use isEmpty utility method instead.", "9:22: Use isEmpty utility method instead.",
        "12:22: Use isNotEmpty utility method instead.", "14:22: Use isNotEmpty utility method instead.",
        "16:22: Use isNotEmpty utility method instead.", "18:22: Use isNotEmpty utility method instead.",
        "21:28: Use isEmpty utility method instead.", "23:28: Use isNotEmpty utility method instead.",
        "26:27: Use isEmpty utility method instead.", "28:27: Use isNotEmpty utility method instead.",
        "31:30: Use isEmpty utility method instead.", "33:30: Use isNotEmpty utility method instead."};

    verify(config(), getPath("UseIsEmptyCheckIssues.jv"), expected);
  }

  @Test
  public void testFalsePositive() throws Exception {
    final String[] expected = {};
    verify(config(), getPath("UseIsEmptyCheckNonIssues.jv"), expected);
  }
}
