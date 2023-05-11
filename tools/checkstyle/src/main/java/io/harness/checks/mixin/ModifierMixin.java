/*
 * Copyright 2019 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks.mixin;

import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_CLASS;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_INTERFACE;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_NATIVE;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_PRIVATE;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_PROTECTED;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_PUBLIC;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_STATIC;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_SYNCHRONIZED;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_TRANSIENT;
import static com.puppycrawl.tools.checkstyle.api.TokenTypes.LITERAL_VOLATILE;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ModifierMixin {
  public static boolean isModifier(DetailAST ast) {
    switch (ast.getType()) {
      case LITERAL_PRIVATE:
      case LITERAL_PUBLIC:
      case LITERAL_PROTECTED:
      case LITERAL_STATIC:
      case LITERAL_TRANSIENT:
      case LITERAL_NATIVE:
      case LITERAL_SYNCHRONIZED:
      case LITERAL_VOLATILE:
      case LITERAL_CLASS:
      case LITERAL_INTERFACE:
        return true;
      default:
        return false;
    }
  }
}
