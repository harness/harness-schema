/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.proto;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class ProtoFileTest {
  @Test
  public void getJavaClassNames_withPackageNameOnly() {
    // Arrange.
    ProtoFile protoFile = ProtoFile
                              .builder(/*packageName=*/"io.harness.test", /*importStatements=*/ImmutableList.of(),
                                  /*messageNames=*/ImmutableList.of("TestProto", "AnotherTestProto"))
                              .build();

    // Act & Assert.
    assertThat(protoFile.getJavaClassNames()).contains("io.harness.test.TestProto", "io.harness.test.AnotherTestProto");
  }

  @Test
  public void getJavaClassNames_withPackageAndJavaPackageName_usesJavaPackageName() {
    // Arrange.
    ProtoFile protoFile = ProtoFile
                              .builder(/*packageName=*/"io.harness.test", /*importStatements=*/ImmutableList.of(),
                                  /*messageNames=*/ImmutableList.of("TestProto", "AnotherTestProto"))
                              .javaPackage("io.harness.testing")
                              .build();

    // Act & Assert.
    assertThat(protoFile.getJavaClassNames())
        .contains("io.harness.testing.TestProto", "io.harness.testing.AnotherTestProto");
  }

  @Test
  public void getJavaClassNames_withJavaOuterClassName_appendsJavaOuterClassNameToFQDN() {
    // Arrange.
    ProtoFile protoFile = ProtoFile
                              .builder(/*packageName=*/"io.harness.test", /*importStatements=*/ImmutableList.of(),
                                  /*messageNames=*/ImmutableList.of("TestProto", "AnotherTestProto"))
                              .javaOuterClassName("AllTests")
                              .build();

    // Act & Assert.
    assertThat(protoFile.getJavaClassNames())
        .contains("io.harness.test.AllTests.TestProto", "io.harness.test.AllTests.AnotherTestProto");
  }
}
