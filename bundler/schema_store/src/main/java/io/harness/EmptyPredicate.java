/*
 * Copyright 2021 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Shield 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/06/PolyForm-Shield-1.0.0.txt.
 */

package io.harness;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.ByteString;
import java.util.Collection;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 * EmptyPredicate provides generic methods that are applicable for wide variety of constructs allowing for
 * static import of the method without risk of name collisions.
 */

public class EmptyPredicate {
  public interface IsEmpty {
    boolean isEmpty();
  }

  public static <T extends IsEmpty> boolean isEmpty(T structure) {
    return structure == null || structure.isEmpty();
  }

  public static <T> boolean isEmpty(Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }

  public static <K, V> boolean isEmpty(Map<K, V> map) {
    return map == null || map.isEmpty();
  }

  public static boolean isEmpty(String string) {
    return string == null || string.isEmpty();
  }

  public static boolean isEmpty(ByteString string) {
    return string == null || string.isEmpty();
  }

  public static boolean isEmpty(Object[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(long[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(int[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(short[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(char[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(byte[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(double[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(float[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(boolean[] array) {
    return array == null || array.length == 0;
  }

  public static boolean isEmpty(Page<?> array) {
    return array == null || array.getSize() == 0;
  }

  public static boolean isEmpty(ObjectNode node) {
    return node == null || node.isEmpty();
  }

  public static boolean isEmpty(ArrayNode node) {
    return node == null || node.isEmpty();
  }

  public static <T extends IsEmpty> boolean isNotEmpty(T structure) {
    return structure != null && !structure.isEmpty();
  }

  public static <T> boolean isNotEmpty(Collection<T> collection) {
    return collection != null && !collection.isEmpty();
  }

  public static <K, V> boolean isNotEmpty(Map<K, V> map) {
    return map != null && !map.isEmpty();
  }

  public static boolean isNotEmpty(String string) {
    return string != null && !string.isEmpty();
  }

  public static boolean isNotEmpty(ByteString string) {
    return string != null && !string.isEmpty();
  }

  public static boolean isNotEmpty(Object[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(long[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(int[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(short[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(char[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(byte[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(double[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(float[] array) {
    return array != null && array.length != 0;
  }

  public static boolean isNotEmpty(boolean[] array) {
    return array != null && array.length != 0;
  }
}
