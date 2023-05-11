/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import io.harness.checks.mixin.FieldMixin;
import io.harness.checks.mixin.GeneralMixin;
import io.harness.checks.mixin.MethodMixin;
import io.harness.checks.mixin.OperationMixin;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;

public class UseIsEmptyCheck extends AbstractCheck {
  private static final String UTIL_NULL_OR_EMPTY_MSG_KEY = "code.readability.util.null_or_empty";
  private static final String UTIL_NOT_NULL_AND_NOT_EMPTY_MSG_KEY = "code.readability.util.not_null_and_not_empty";

  @Override
  public int[] getDefaultTokens() {
    return new int[] {
        TokenTypes.LOR,
        TokenTypes.LAND,
        TokenTypes.EQUAL,
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

  void checkForNullOrIsEmpty(DetailAST ast) {
    if (ast.getType() != TokenTypes.LOR) {
      return;
    }

    DetailAST parent = ast.getParent();
    while (parent != null) {
      if (parent.getType() == TokenTypes.LITERAL_RETURN) {
        return;
      }
      parent = parent.getParent();
    }

    final List<DetailAST> list = OperationMixin.list(ast, TokenTypes.LOR);
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        final DetailAST left = list.get(i);
        final DetailAST leftStatement = OperationMixin.equalNull(left);
        if (leftStatement == null) {
          continue;
        }
        final DetailAST right = list.get(j);
        DetailAST rightStatement = MethodMixin.call(right, "isEmpty");
        if (rightStatement == null) {
          DetailAST zero = OperationMixin.equalZero(right);
          if (zero == null) {
            continue;
          }

          rightStatement = FieldMixin.field(zero, "length");
          if (rightStatement == null) {
            continue;
          }
        }
        if (!GeneralMixin.same(leftStatement, rightStatement)) {
          continue;
        }
        log(ast, UTIL_NULL_OR_EMPTY_MSG_KEY);
      }
    }
  }

  void checkForNotNullAndIsNotEmpty(DetailAST ast) {
    if (ast.getType() != TokenTypes.LAND) {
      return;
    }

    DetailAST parent = ast.getParent();
    while (parent != null) {
      if (parent.getType() == TokenTypes.LITERAL_RETURN) {
        return;
      }
      parent = parent.getParent();
    }

    final List<DetailAST> list = OperationMixin.list(ast, TokenTypes.LAND);
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        final DetailAST left = list.get(i);
        final DetailAST leftStatement = OperationMixin.notEqualNull(left);
        if (leftStatement == null) {
          continue;
        }
        final DetailAST right = list.get(j);
        DetailAST rightStatement = right.getType() != TokenTypes.LNOT
            ? MethodMixin.call(right, "isNotEmpty")
            : MethodMixin.call(right.getFirstChild(), "isEmpty");
        if (rightStatement == null) {
          DetailAST notZero = OperationMixin.notEqualZero(right);
          if (notZero == null) {
            continue;
          }

          rightStatement = FieldMixin.field(notZero, "length");
          if (rightStatement == null) {
            continue;
          }
        }

        if (!GeneralMixin.same(leftStatement, rightStatement)) {
          continue;
        }

        log(ast, UTIL_NOT_NULL_AND_NOT_EMPTY_MSG_KEY);
      }
    }
  }

  @Override
  public void visitToken(DetailAST ast) {
    checkForNullOrIsEmpty(ast);
    checkForNotNullAndIsNotEmpty(ast);
  }
}
