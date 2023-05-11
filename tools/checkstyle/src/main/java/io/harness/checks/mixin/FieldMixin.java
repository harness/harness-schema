/*
 * Copyright 2018 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks.mixin;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class FieldMixin {
  public static DetailAST field(DetailAST ast, String name) {
    if (ast.getType() != TokenTypes.DOT) {
      return null;
    }

    final DetailAST statement = ast.getFirstChild();
    final DetailAST field = statement.getNextSibling();
    if (field.getType() != TokenTypes.IDENT || !field.getText().equals(name)) {
      return null;
    }

    return statement;
  }
}
