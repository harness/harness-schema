/*
 * Copyright 2022 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.buildcleaner;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenManifest {
  private static final Logger logger = LoggerFactory.getLogger(BuildCleaner.class);
  private final HashMap<String, List<String>> packageToTargetMap = new HashMap<>();

  public void addPackageToTargetMap(String pkg, String target) {
    List<String> validTargets = packageToTargetMap.getOrDefault(pkg, new ArrayList<>());
    validTargets.add(target);
    packageToTargetMap.putIfAbsent(pkg, validTargets);
  }

  public Optional<String> getTarget(String fqdn) {
    return getTargetForPackage(fqdn.substring(0, fqdn.lastIndexOf(".")));
  }

  private Optional<String> getTargetForPackage(String pkg) {
    List<String> validTargets = packageToTargetMap.getOrDefault(pkg, Collections.emptyList());
    if (validTargets.isEmpty()) {
      return Optional.empty();
    }

    if (validTargets.size() > 1) {
      logger.warn("Pkg '{}' has multiple targets: {}", pkg, validTargets);
    }
    return Optional.of(validTargets.get(0));
  }

  public static MavenManifest loadFromFile(Path path) {
    MavenManifest mavenManifest = new MavenManifest();

    try (Reader baseReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())))) {
      JsonObject artifactsMappingObj = JsonParser.parseReader(baseReader)
                                           .getAsJsonObject()
                                           .get("manifest")
                                           .getAsJsonObject()
                                           .get("artifacts_mapping")
                                           .getAsJsonObject();

      for (String packageName : artifactsMappingObj.keySet()) {
        for (Iterator<JsonElement> it = artifactsMappingObj.get(packageName).getAsJsonArray().iterator();
             it.hasNext();) {
          JsonElement target = it.next();
          mavenManifest.addPackageToTargetMap(packageName, target.getAsString());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not open manifest file: " + path);
    }

    return mavenManifest;
  }

  public static MavenManifest loadFromOverrideFile(Path path) {
    MavenManifest mavenManifest = new MavenManifest();

    try (BufferedReader baseReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString())))) {
      String line;
      while ((line = baseReader.readLine()) != null) {
        mavenManifest.addPackageToTargetMap(
            line.substring(0, line.lastIndexOf(',')), line.substring(line.lastIndexOf(',') + 1));
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not open override file: " + path);
    }

    return mavenManifest;
  }
}
