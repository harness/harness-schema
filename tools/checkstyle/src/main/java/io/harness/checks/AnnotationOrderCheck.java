/*
 * Copyright 2021 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import io.harness.checks.mixin.AnnotationMixin;
import io.harness.checks.mixin.ModifierMixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationOrderCheck extends AbstractCheck {
  private static final String COMMENT_MSG_KEY = "annotation.problem.comment";
  private static final String ORDER_MSG_KEY = "code.readability.annotation.order";
  private static final String MODIFIER_MSG_KEY = "code.readability.annotation.modifier";
  private static final String REQUIRED_MSG_KEY = "code.readability.annotation.required.missing";
  private static final String INCOMPATIBLE_MSG_KEY = "annotation.problem.incompatible";

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

  // Negative values go over the unknown annotations
  // Positive value go under the unknown annotations
  Map<String, Integer> annotationOrder = ImmutableMap.<String, Integer>builder()
                                             .put("Value", -99)
                                             .put("Data", -99)
                                             .put("Getter", -98)
                                             .put("Setter", -97)
                                             .put("Builder", -89)
                                             .put("NoArgsConstructor", -88)
                                             .put("AllArgsConstructor", -87)
                                             .put("ToString", -79)
                                             .put("EqualsAndHashCode", -78)
                                             .put("Test", -69)
                                             .put("Owner", -68)
                                             .put("Repeat", -67)
                                             .put("Category", -66)
                                             .put("Ignore", -65)
                                             .put("JsonIgnore", -59)
                                             .put("SchemaIgnore", -49)
                                             .put("Entity", -39)
                                             .put("Document", -39)
                                             .put("StoreIn", -40)
                                             .put("HarnessEntity", -38)
                                             .put("UtilityClass", -9)
                                             .put("Slf4j", -8)
                                             .put("Deprecated", 1000)
                                             .build();

  Map<String, Map<String, String>> incompatible =
      ImmutableMap.<String, Map<String, String>>builder()
          .put("SchemaIgnore",
              ImmutableMap.<String, String>builder()
                  .put("Getter",
                      "Lombok is not propagating the field annotations when creating getter methods. "
                          + "This will result of @SchemaIgnore annotation to be noop. You cannot use them on the same field.")
                  .build())
          .build();

  Map<String, Set<String>> required = ImmutableMap.<String, Set<String>>builder()
                                          .put("Ignore", ImmutableSet.<String>builder().add("Owner").build())
                                          .put("HarnessEntity", ImmutableSet.<String>builder().add("Entity").build())
                                          .put("Entity", ImmutableSet.<String>builder().add("StoreIn").build())
                                          .put("Document", ImmutableSet.<String>builder().add("StoreIn").build())
                                          .build();

  @Override
  public int[] getAcceptableTokens() {
    return getDefaultTokens();
  }

  @Override
  public void visitToken(DetailAST annotation) {
    final String name = AnnotationMixin.name(annotation);

    // TODO: the following is not working to detect comments between the annotation - fix it.
    DetailAST nextAST = annotation.getNextSibling();
    if (nextAST != null && nextAST.getType() == TokenTypes.COMMENT_CONTENT) {
      log(annotation, COMMENT_MSG_KEY, name);
    }
    final Integer order = annotationOrder.getOrDefault(name, 0);

    Map<String, String> incompatibleMap = incompatible.get(name);
    Set<String> requiredSet = new HashSet<>(required.getOrDefault(name, new HashSet<>()));

    DetailAST prevAnnotation = annotation.getPreviousSibling();
    while (prevAnnotation != null && prevAnnotation.getType() == TokenTypes.ANNOTATION) {
      final String prevName = AnnotationMixin.name(prevAnnotation);
      requiredSet.remove(prevName);

      if (incompatibleMap != null) {
        String msg = incompatibleMap.get(prevName);
        if (msg != null) {
          log(annotation, INCOMPATIBLE_MSG_KEY, name, prevName, msg);
        }
      }

      prevAnnotation = prevAnnotation.getPreviousSibling();

      final Integer prevOrder = annotationOrder.get(prevName);
      if (prevOrder == null) {
        continue;
      }

      if (prevOrder > order) {
        log(annotation, ORDER_MSG_KEY, name, prevName);
      }
    }

    if (!requiredSet.isEmpty()) {
      log(annotation, REQUIRED_MSG_KEY, name, String.join(", ", requiredSet));
    }

    if (prevAnnotation != null && ModifierMixin.isModifier(prevAnnotation)) {
      log(annotation, MODIFIER_MSG_KEY, name);
    }
  }
}
