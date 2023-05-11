/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.monitoring;

import com.google.api.Metric;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.util.Timestamps;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleCloudMonitoring {
  private static final String CREDS_FILE_KEY = "CREDS_FILE";
  private static final String METRIC_TYPE_PREFIX = "custom.googleapis.com";
  private static final String PROJECT_ID = "platform-205701";

  private static final String PR = "pr";
  private static final String CHECK_TOTAL_TIME_METRIC_NAME = "/bnt/pr/checkTotalTime";
  private static final String CHECK_EXECUTION_TIME_METRIC_NAME = "/bnt/pr/checkExecutionTime";
  private static final String CHECK_TOTAL_TIME_KEY = "CHECK_TOTAL_TIME";
  private static final String CHECK_EXECUTION_TIME_KEY = "CHECK_EXECUTION_TIME";
  private static final String CHECK_KEY = "CHECK";
  private static final String PR_KEY = "PR";
  private static final String BUILD_KEY = "BUILD";
  private static final String COMMIT_KEY = "COMMIT";

  private static boolean isEmpty(String string) {
    return string == null || string.length() == 0;
  }

  private static String readEnvVar(String key) throws Exception {
    String value = System.getenv(key);
    if (isEmpty(value)) {
      throw new Exception(String.format("Could not find env variable: [%s]", key));
    }
    return value;
  }

  private static void addBnTDevDisruptionVariables(String metricName, String metricValueKey) throws Exception {
    try (MetricServiceClient client = MetricServiceClient.create(
             MetricServiceSettings.newBuilder()
                 .setCredentialsProvider(FixedCredentialsProvider.create(
                     ServiceAccountCredentials.fromStream(new FileInputStream(readEnvVar(CREDS_FILE_KEY)))))
                 .build())) {
      List<Point> points = Collections.singletonList(
          Point.newBuilder()
              .setInterval(
                  TimeInterval.newBuilder().setEndTime(Timestamps.fromMillis(System.currentTimeMillis())).build())
              .setValue(TypedValue.newBuilder().setDoubleValue(Double.parseDouble(readEnvVar(metricValueKey))).build())
              .build());

      Map<String, String> metricLabels = new HashMap<>();
      metricLabels.put(CHECK_KEY, readEnvVar(CHECK_KEY));
      metricLabels.put(PR_KEY, readEnvVar(PR_KEY));
      metricLabels.put(BUILD_KEY, readEnvVar(BUILD_KEY));
      metricLabels.put(COMMIT_KEY, readEnvVar(COMMIT_KEY));

      String metricType = METRIC_TYPE_PREFIX + metricName;
      Metric metric = Metric.newBuilder().setType(metricType).putAllLabels(metricLabels).build();

      TimeSeries timeSeries = TimeSeries.newBuilder().setMetric(metric).addAllPoints(points).build();

      List<TimeSeries> timeSeriesList = new ArrayList<>();
      timeSeriesList.add(timeSeries);

      ProjectName name = ProjectName.of(PROJECT_ID);

      CreateTimeSeriesRequest request =
          CreateTimeSeriesRequest.newBuilder().setName(name.toString()).addAllTimeSeries(timeSeriesList).build();

      client.createTimeSeries(request);
    }
  }

  /*
  public static void main(String[] args) {
    uploadFailedTestsData(Arrays.asList(
        FailedTestData.builder().author("satyam.shanker@harness.io").team("CDP").numFailedTests(2).build(),
        FailedTestData.builder().author("user1@harness.io").team("CDP").numFailedTests(1).build(),
        FailedTestData.builder().author("user2@harness.io").team("CDC").numFailedTests(1).build()
    ));
  }
  */

  public static void uploadFailedTestsData(List<FailedTestData> failedTests) {
    if (failedTests == null || failedTests.size() == 0) {
      return;
    }
    try (MetricServiceClient client = MetricServiceClient.create(
             MetricServiceSettings.newBuilder()
                 .setCredentialsProvider(FixedCredentialsProvider.create(
                     ServiceAccountCredentials.fromStream(new FileInputStream(readEnvVar(CREDS_FILE_KEY)))))
                 .build())) {
      failedTests.forEach(failedTest -> {
        List<Point> points = Collections.singletonList(
            Point.newBuilder()
                .setInterval(
                    TimeInterval.newBuilder().setEndTime(Timestamps.fromMillis(System.currentTimeMillis())).build())
                .setValue(TypedValue.newBuilder().setInt64Value(failedTest.getNumFailedTests()).build())
                .build());
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AUTHOR", failedTest.getAuthor());
        metricLabels.put("TEAM", failedTest.getTeam());

        String metricType = METRIC_TYPE_PREFIX + "/bnt/test/failedTests";
        Metric metric = Metric.newBuilder().setType(metricType).putAllLabels(metricLabels).build();

        TimeSeries timeSeries = TimeSeries.newBuilder().setMetric(metric).addAllPoints(points).build();
        List<TimeSeries> timeSeriesList = new ArrayList<>();
        timeSeriesList.add(timeSeries);

        ProjectName name = ProjectName.of(PROJECT_ID);
        CreateTimeSeriesRequest request =
            CreateTimeSeriesRequest.newBuilder().setName(name.toString()).addAllTimeSeries(timeSeriesList).build();
        client.createTimeSeries(request);
      });
    } catch (Exception ex) {
      log.error(String.format("Exception while uploading failed tests data: [%s]", ex.getMessage()), ex);
    }
  }

  public static void uploadMetrics(String[] args) throws Exception {
    if (args.length < 2) {
      throw new UnsupportedOperationException("Need upload operation metrics");
    }
    switch (args[1]) {
      case PR: {
        log.info("Uploading: " + CHECK_EXECUTION_TIME_KEY);
        addBnTDevDisruptionVariables(CHECK_EXECUTION_TIME_METRIC_NAME, CHECK_EXECUTION_TIME_KEY);
        log.info("Uploading: " + CHECK_TOTAL_TIME_KEY);
        addBnTDevDisruptionVariables(CHECK_TOTAL_TIME_METRIC_NAME, CHECK_TOTAL_TIME_KEY);
        break;
      }
      default: {
        throw new UnsupportedOperationException("Did not recognise option: " + args[1]);
      }
    }
    log.info("Finished uploading metrics");
  }
}
