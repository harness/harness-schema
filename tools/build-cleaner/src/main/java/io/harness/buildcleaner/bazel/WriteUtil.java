/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.bazel;

import java.util.SortedSet;

public class WriteUtil {
  public static final String INDENTATION = "    ";

  public static void updateResponseWithSet(
      SortedSet<String> collection, String name, StringBuilder response, boolean leadingIndent) {
    if (leadingIndent) {
      response.append(INDENTATION);
    }
    response.append(name).append(" = [");
    if (collection.size() > 1) {
      response.append("\n");
    }
    for (String entity : collection) {
      if (collection.size() > 1) {
        response.append(INDENTATION).append(INDENTATION);
      }
      response.append("\"").append(entity).append("\"");
      if (collection.size() > 1) {
        response.append(",\n");
      }
    }
    if (collection.size() > 1) {
      response.append(INDENTATION);
    }
    response.append("]");

    if (leadingIndent) {
      response.append(",\n");
    }
  }
}