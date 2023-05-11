/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.Map;

public class WingsExceptionCheck extends AbstractCheck {
  private static final String MSG_KEY = "exception.use.specific.class";

  final Map<String, String> codes =
      ImmutableMap.of("INVALID_REQUEST", "InvalidRequestException", "KMS_OPERATION_ERROR", "KmsOperationException");

  @Override
  public int[] getDefaultTokens() {
    return new int[] {
        TokenTypes.IDENT,
    };
  }

  @Override
  public int[] getRequiredTokens() {
    return new int[] {
        TokenTypes.IDENT,
    };
  }

  @Override
  public int[] getAcceptableTokens() {
    return getDefaultTokens();
  }

  @Override
  public void visitToken(DetailAST identifier) {
    if (!codes.containsKey(identifier.getText())) {
      return;
    }

    DetailAST expression = identifier.getParent();
    if (expression.getType() == TokenTypes.DOT) {
      expression = expression.getParent();
    }
    if (expression.getType() != TokenTypes.EXPR) {
      return;
    }

    final DetailAST elist = expression.getParent();
    if (elist.getType() != TokenTypes.ELIST) {
      return;
    }

    final DetailAST exception = elist.getPreviousSibling().getPreviousSibling();
    if (exception == null || exception.getType() != TokenTypes.IDENT || !exception.getText().equals("WingsException")) {
      return;
    }

    log(identifier, MSG_KEY, codes.get(identifier.getText()));
  }
}
