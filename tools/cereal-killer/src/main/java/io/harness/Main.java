/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness;

import static java.util.Collections.emptySet;

import io.harness.checks.FlakeFinder;
import io.harness.checks.ReportFinder;
import io.harness.checks.ReportProcessor;
import io.harness.checks.buildpulse.client.BuildPulseClient;
import io.harness.monitoring.GoogleCloudMonitoring;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.xml.sax.SAXException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Slf4j
public class Main {
  private static BuildPulseClient getBuildPulseClient(String baseUrl, String authToken) {
    Duration halfMinute = Duration.ofSeconds(30);
    OkHttpClient okHttpClient =
        new OkHttpClient()
            .newBuilder()
            .connectTimeout(halfMinute)
            .readTimeout(halfMinute)
            .writeTimeout(halfMinute)
            .addInterceptor(chain
                -> chain.proceed(chain.request().newBuilder().header("Authorization", "token " + authToken).build()))
            .build();
    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
    return retrofit.create(BuildPulseClient.class);
  }

  public static void main(String[] args)
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, TransformerException {
    String phase = args[0];
    switch (phase) {
      case "check":
        check(args);
        break;
      case "removeSkipped":
        removeSkipped(args);
        break;
      case "suppressFlakes":
        suppressFlakes(args);
        break;
      case "uploadMetrics":
        uploadMetrics(args);
        break;
      default:
        throw new UnsupportedOperationException("Unknown operation " + phase);
    }
  }

  private static void uploadMetrics(String[] args) {
    try {
      // java -jar cereal-killer.jar uploadMetrics dev-disruption
      GoogleCloudMonitoring.uploadMetrics(args);
    } catch (Exception ex) {
      log.error(String.format("Exception while uploading metrics: [%s]", ex.getMessage()), ex);
    }
  }

  private static void removeSkipped(String[] args)
      throws IOException, ParserConfigurationException, SAXException, TransformerException, XPathExpressionException {
    String baseDir = args[1];
    ReportProcessor reportProcessor = new ReportProcessor(emptySet());
    List<String> surefireReports = new ReportFinder(baseDir).findSurefireReports();
    for (String report : surefireReports) {
      log.info("Processing report file {}", report);
      reportProcessor.removeFlakyTestsAndCheckSuccess(report);
    }
  }

  private static void suppressFlakes(String[] args)
      throws IOException, ParserConfigurationException, SAXException, TransformerException, XPathExpressionException {
    String url = Objects.requireNonNull(System.getenv("BUILDPULSE_URL"), "BUILDPULSE_URL missing");
    String token = Objects.requireNonNull(System.getenv("BUILDPULSE_TOKEN"), "BUILDPULSE_TOKEN missing");
    String baseDir = args[1];
    double maxFailChance = Double.parseDouble(args[2]);
    BuildPulseClient buildPulseClient = getBuildPulseClient(url, token);
    Set<String> flakyTests = new FlakeFinder(buildPulseClient, maxFailChance).fetchFlakyTests();
    log.info("Found {} flaky tests with maxFailChance = {}", flakyTests.size(), maxFailChance);
    log.info("Flaky tests are: \n{}", String.join("\n", flakyTests));
    List<String> surefireReports = new ReportFinder(baseDir).findSurefireReports();
    ReportProcessor reportProcessor = new ReportProcessor(flakyTests);
    boolean success = true;
    for (String report : surefireReports) {
      log.info("Processing report file {}", report);
      success = reportProcessor.removeFlakyTestsAndCheckSuccess(report) && success;
    }
    if (!success) {
      log.warn("Found non-flaky test failures");
    }
  }

  private static void check(String[] args)
      throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
    String baseDir = args[1];
    int maxFailures = Integer.parseInt(args[2]);
    List<String> surefireReports = new ReportFinder(baseDir).findSurefireReports();
    ReportProcessor reportProcessor = new ReportProcessor(null);
    int numFailures = 0;
    for (String report : surefireReports) {
      int failureCount = reportProcessor.getFailureCount(report);

      // keep this report significantly different the issue one to be searchable
      if (failureCount == 0) {
        log.info("{} - is clean", report);
      } else {
        log.info("{} - has {} issue(s)", report, failureCount);
      }
      numFailures += failureCount;
    }
    log.info("Total number of failed tests: {}", numFailures);
    if (numFailures >= maxFailures) {
      log.error("Too many test failures");
      System.exit(1);
    }
  }
}
