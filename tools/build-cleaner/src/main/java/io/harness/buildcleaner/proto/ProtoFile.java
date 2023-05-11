/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.proto;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ProtoFile {
  @NonNull private final String packageName;
  private final String javaPackage;
  private final String javaOuterClassName;
  @NonNull private final ImmutableList<String> importStatements;
  @NonNull private final ImmutableList<String> messageNames;

  public static ProtoFileBuilder builder(
      String packageName, ImmutableList<String> importStatements, ImmutableList<String> messageNames) {
    return new io.harness.buildcleaner.proto.ProtoFile.ProtoFileBuilder()
        .packageName(packageName)
        .importStatements(importStatements)
        .messageNames(messageNames);
  }

  /**
   * Once proto file is parsed, this function would return the FQDN of the Java classes to be generated.
   * @return list of the java classes which would be generated from proto files.
   */
  public List<String> getJavaClassNames() {
    return messageNames.stream().map(this::constructClassName).collect(toList());
  }

  private String constructClassName(String messageName) {
    StringBuilder className = new StringBuilder().append(javaPackage == null ? packageName : javaPackage).append('.');
    if (javaOuterClassName != null) {
      className.append(javaOuterClassName).append('.');
    }
    className.append(messageName);
    return className.toString();
  }
}
