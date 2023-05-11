/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks.buildpulse.client;

import io.harness.checks.buildpulse.dto.TestFlakinessList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BuildPulseClient {
  @GET("/api/repos/{org}/{repo}/tests")
  Call<TestFlakinessList> listFlakyTests(@Path("org") String org, @Path("repo") String repo);
}
