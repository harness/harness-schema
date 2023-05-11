/*
 * Copyright 2021 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import static io.harness.checks.AnnotationParametersCheck.Expectation.Type.EXISTS;
import static io.harness.checks.AnnotationParametersCheck.Expectation.Type.EXISTS_REGEX_MATCH;

import static java.util.Arrays.asList;

import io.harness.checks.mixin.AnnotationMixin;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.List;
import java.util.Map;

public class AnnotationParametersCheck extends AbstractCheck {
  private static final String MSG_KEY = "annotation.parameter.expectation.not.met";

  public static class Expectation {
    public enum Type { EXISTS, EXISTS_REGEX_MATCH }

    public String annotationName;
    public String parameter;
    public boolean defaultParameter;
    public Type type;
    public String regex;

    public Expectation(String annotationName, String parameter, boolean defaultParameter, Type type, String regex) {
      this.annotationName = annotationName;
      this.parameter = parameter;
      this.defaultParameter = defaultParameter;
      this.type = type;
      this.regex = regex;
    }

    public boolean isMet(Map<String, DetailAST> parameters) {
      if (type == EXISTS) {
        if (defaultParameter) {
          if (parameters.containsKey("")) {
            return true;
          }
        }

        if (parameters.containsKey(parameter)) {
          return true;
        }

        return false;
      }

      if (type == EXISTS_REGEX_MATCH) {
        if (defaultParameter) {
          final DetailAST expression = parameters.get("");
          if (expression != null) {
            final String defaultValue = AnnotationMixin.obtainDefaultValue(expression);
            return defaultValue.matches(regex);
          }
        }

        if (parameters.containsKey(parameter)) {
          // TODO: check the value
          return true;
        }

        return false;
      }

      return true;
    }

    public String toString() {
      if (type == EXISTS) {
        if (defaultParameter) {
          return String.format("The default parameter %s is not provided", parameter);
        }
        return String.format("Parameter %s is not provided", parameter);
      }

      if (type == EXISTS_REGEX_MATCH) {
        if (defaultParameter) {
          return String.format("The default parameter %s does not match the expected pattern '%s'", parameter, regex);
        }
        return String.format("Parameter %s does not match the expected pattern '%s'", parameter, regex);
      }

      return "***************";
    }
  }

  private static final List<Expectation> execpectations =
      asList(new Expectation("FieldNameConstants", "innerTypeName", false, EXISTS, ""),
          new Expectation("Ignore", "value", true, EXISTS_REGEX_MATCH, ".{25,110}"));

  @Override
  public int[] getDefaultTokens() {
    return new int[] {
        TokenTypes.ANNOTATION,
    };
  }

  @Override
  public int[] getRequiredTokens() {
    return new int[] {
        TokenTypes.ANNOTATION,
    };
  }

  @Override
  public int[] getAcceptableTokens() {
    return getDefaultTokens();
  }

  @Override
  public void visitToken(DetailAST annotationNode) {
    final String annotationNameCheck = AnnotationMixin.name(annotationNode);

    for (Expectation expectation : execpectations) {
      if (!annotationNameCheck.equals(expectation.annotationName)) {
        continue;
      }

      Map<String, DetailAST> parameters = AnnotationMixin.parameters(annotationNode);
      if (expectation.isMet(parameters)) {
        continue;
      }

      log(annotationNode, MSG_KEY, expectation.annotationName, expectation.toString());
    }
  }
}
