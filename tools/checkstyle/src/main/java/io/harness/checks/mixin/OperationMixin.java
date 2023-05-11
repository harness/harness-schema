/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks.mixin;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.ArrayList;
import java.util.List;

public class OperationMixin {
  public interface AstPredicate {
    boolean check(DetailAST ast);
  }

  public static List<DetailAST> list(DetailAST ast, int operation) {
    List<DetailAST> result = new ArrayList<>();
    if (ast.getType() != operation) {
      result.add(ast);
      return result;
    }
    final DetailAST left = ast.getFirstChild();
    result.addAll(list(left, operation));
    final DetailAST right = left.getNextSibling();
    result.addAll(list(right, operation));

    return result;
  }

  public static DetailAST transitive(DetailAST ast, int operation, AstPredicate object, AstPredicate something) {
    if (ast.getType() != operation) {
      return null;
    }

    DetailAST first = ast.getFirstChild();
    DetailAST second = first.getNextSibling();

    if (object.check(first) && something.check(second)) {
      return first;
    }

    if (object.check(second) && something.check(first)) {
      return second;
    }

    return null;
  }

  public static DetailAST equalNull(DetailAST ast) {
    return transitive(ast, TokenTypes.EQUAL, child -> true, constant -> constant.getType() == TokenTypes.LITERAL_NULL);
  }

  public static DetailAST identifierEqualNull(DetailAST ast) {
    return transitive(ast, TokenTypes.EQUAL,
        identifier
        -> identifier.getType() == TokenTypes.IDENT,
        constant -> constant.getType() == TokenTypes.LITERAL_NULL);
  }

  public static DetailAST notEqualNull(DetailAST ast) {
    return transitive(
        ast, TokenTypes.NOT_EQUAL, child -> true, constant -> constant.getType() == TokenTypes.LITERAL_NULL);
  }

  public static DetailAST identifierNotEqualNull(DetailAST ast) {
    return transitive(ast, TokenTypes.NOT_EQUAL,
        identifier
        -> identifier.getType() == TokenTypes.IDENT,
        constant -> constant.getType() == TokenTypes.LITERAL_NULL);
  }

  public static DetailAST equalZero(DetailAST ast) {
    return transitive(ast, TokenTypes.EQUAL,
        child -> true, constant -> constant.getType() == TokenTypes.NUM_INT && constant.getText().equals("0"));
  }

  public static DetailAST notEqualZero(DetailAST ast) {
    return transitive(ast, TokenTypes.NOT_EQUAL,
        child -> true, constant -> constant.getType() == TokenTypes.NUM_INT && constant.getText().equals("0"));
  }
}
