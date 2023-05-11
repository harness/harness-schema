/*
 * Copyright 2018 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class LoggerCheck extends AbstractCheck {
  private static final String NON_STATIC_LOGGER_MSG_KEY = "performance.non-static.logger";

  @Override
  public int[] getDefaultTokens() {
    return new int[] {
        TokenTypes.TYPE,
    };
  }

  @Override
  public int[] getRequiredTokens() {
    return getDefaultTokens();
  }

  @Override
  public int[] getAcceptableTokens() {
    return getDefaultTokens();
  }

  @Override
  public void visitToken(DetailAST ast) {
    if (ast.getChildCount() != 1) {
      return;
    }
    final DetailAST loggerTypeIdentifier = ast.getFirstChild();
    if (loggerTypeIdentifier.getType() != TokenTypes.IDENT || !loggerTypeIdentifier.getText().equals("Logger")) {
      return;
    }

    DetailAST loggerIdentifier = ast.getNextSibling();
    if (loggerIdentifier.getType() != TokenTypes.IDENT || !loggerIdentifier.getText().equals("logger")) {
      return;
    }

    DetailAST assign = loggerIdentifier.getNextSibling();
    if (assign == null || assign.getType() != TokenTypes.ASSIGN) {
      return;
    }

    DetailAST expression = assign.getFirstChild();
    if (expression.getType() != TokenTypes.EXPR) {
      return;
    }

    DetailAST method = expression.getFirstChild();
    if (method.getType() != TokenTypes.METHOD_CALL) {
      return;
    }

    DetailAST dot = method.getFirstChild();
    if (dot.getType() != TokenTypes.DOT) {
      return;
    }

    DetailAST loggerFactoryIdentifier = dot.getFirstChild();
    if (loggerFactoryIdentifier.getType() != TokenTypes.IDENT
        || !loggerFactoryIdentifier.getText().equals("LoggerFactory")) {
      return;
    }

    DetailAST previousSibling = ast.getPreviousSibling();
    while (previousSibling != null && previousSibling.getType() != TokenTypes.MODIFIERS) {
      previousSibling = previousSibling.getPreviousSibling();
    }

    if (previousSibling == null) {
      log(ast, NON_STATIC_LOGGER_MSG_KEY);
      return;
    }

    DetailAST modifier = previousSibling.getFirstChild();
    while (modifier != null && modifier.getType() != TokenTypes.LITERAL_STATIC) {
      modifier = modifier.getNextSibling();
    }

    if (modifier == null || modifier.getType() != TokenTypes.LITERAL_STATIC) {
      log(ast, NON_STATIC_LOGGER_MSG_KEY);
    }
  }
}
