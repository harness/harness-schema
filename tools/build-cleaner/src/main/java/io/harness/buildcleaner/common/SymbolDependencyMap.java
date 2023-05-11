/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

public class SymbolDependencyMap implements Serializable {
  private final HashMap<String, String> symbolToTargetMap;

  public SymbolDependencyMap() {
    symbolToTargetMap = new HashMap<>();
  }

  public SymbolDependencyMap(HashMap<String, String> symbolToTargetMap) {
    this.symbolToTargetMap = symbolToTargetMap;
  }

  public void addSymbolTarget(String symbol, String target) {
    symbolToTargetMap.put(symbol, target);
  }

  public HashMap<String, String> getSymbolToTargetMap() {
    return symbolToTargetMap;
  }

  public Optional<String> getTarget(String symbol) {
    if (symbolToTargetMap.containsKey(symbol)) {
      return Optional.of(symbolToTargetMap.get(symbol));
    }
    // Keep nesting inside to search for package. Assumption is that imported symbol starts with Capital character.
    while (!symbol.isEmpty() && Character.isUpperCase(symbol.substring(symbol.lastIndexOf('.') + 1).toCharArray()[0])) {
      symbol = symbol.substring(0, symbol.lastIndexOf('.'));
      if (symbolToTargetMap.containsKey(symbol)) {
        return Optional.of(symbolToTargetMap.get(symbol));
      }
    }

    return Optional.empty();
  }

  public int getCacheSize() {
    return symbolToTargetMap.size();
  }

  public void serializeToFile(String path) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(path); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(symbolToTargetMap);
      oos.flush();
    }
  }

  public static SymbolDependencyMap deserializeFromFile(String path) throws IOException, ClassNotFoundException {
    try (FileInputStream fis = new FileInputStream(path); ObjectInputStream ois = new ObjectInputStream(fis)) {
      return new SymbolDependencyMap((HashMap) ois.readObject());
    }
  }
}
