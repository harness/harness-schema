# CDEvents for Harness Pipeline Executions

This directory contains templates and documentation for implementing [CDEvents](https://cdevents.dev/) with Harness Pipeline notifications.

## Overview

CDEvents is a common specification for Continuous Delivery events, providing a standardized format for events that occur during software delivery. By using these templates, Harness users can configure their pipeline notifications to be compatible with the CDEvents specification, enabling better interoperability with other tools in the CI/CD ecosystem.

### What are CDEvents?

CDEvents is an open-source specification for standardizing events in continuous delivery systems. The specification is maintained by the [CDEvents project](https://cdevents.dev/).

### Why Support CDEvents?

Several enterprise customers are adopting CDEvents as a standard for their CI/CD event communication. Supporting CDEvents allows:

- Improved interoperability with other CI/CD tools
- Standardized event formats for analytics and monitoring
- Remove any vendor lock-in

## Directory Structure

```
/cdevents/
├── README.md                           # Main documentation
├── templates/                          # JSON templates
│   ├── build_started.json
│   ├── pipelinerun_started.json
│   ├── pipelinerun_finished_success.json
│   └── pipelinerun_finished_failure.json
└── conformance/                        # Example outputs
    ├── README.md
    ├── build_started.json
    ├── pipelinerun_started.json
    ├── pipelinerun_finished_success.json
    └── pipelinerun_finished_failure.json
```

## CDEvents Types Covered

This repository provides example templates for the following CDEvents types:

- Build/pipeline start events ([template](./templates/build_started.json), [example](./conformance/build_started.json))
- Pipeline run start events ([template](./templates/pipelinerun_started.json), [example](./conformance/pipelinerun_started.json))
- Pipeline run completion events
  - Success outcome ([template](./templates/pipelinerun_finished_success.json), [example](./conformance/pipelinerun_finished_success.json))
  - Failure outcome ([template](./templates/pipelinerun_finished_failure.json), [example](./conformance/pipelinerun_finished_failure.json))

Note that these are non-exhaustive examples of CDEvents that can be implemented with Harness. The templates can be adapted for other event types as needed.

## Implementation

### Template Structure

All templates follow the [CDEvents JSON schema](https://github.com/cdevents/spec/tree/main/schemas) structure, Harness custom notification template can be modified to support CDEvents, example:

```json
{
  "context": {
    "version": "0.5.0-draft",
    "id": "<+pipeline.executionId>",
    "source": "<+pipeline.execution.url>",
    "type": "dev.cdevents.pipelinerun.started.0.3.0-draft",
    "timestamp": "<+pipeline.startTs>"
  },
  "subject": {
    "id": "<+pipeline.identifier>",
    "source": "<+pipeline.execution.url>",
    "type": "pipelineRun",
    "content": {
      "pipelineName": "<+pipeline.name>",
      "uri": "<+pipeline.execution.url>"
    }
  },
  "customData": {
    "accountId": "<+account.identifier>",
    "orgId": "<+org.identifier>",
    "projectId": "<+project.identifier>",
    "triggeredBy": "<+pipeline.triggeredBy.name>",
    "executionId": "<+pipeline.executionId>"
  }
}
```

### Implementation Steps for webhook notification

1. **Configure a Webhook Notification**
   - Navigate to your Harness account settings
   - Go to **Account Settings** > **Connectors** > **Webhooks**
   - Create a new webhook connector or select an existing one
   - Configure the webhook URL where the harness notifications will be sent

2. **Create a Custom Notification Template**
   - Go to **Account Settings** > **Account Resources** > **Templates**
   - Click **New Template**
   - Select **Notification Template** as the template type
   - Choose **Webhook** as the notification channel
   - Copy and paste the appropriate template from the [templates](./templates/) directory
   - Save the template with a descriptive name (e.g., "CDEvents Pipeline Started")

3. **Configure Notification Rules**
   - Go to **Account Settings** > **Account Resources** > **Notification Settings**
   - Create a new notification rule or edit an existing one
   - Select the appropriate trigger (e.g., "Pipeline Execution Started")
   - Choose the custom template you created in step 2
   - Configure the notification recipients (webhook endpoint)
   - Save the notification rule

## Variables

### Built-in Harness Variables

The templates use Harness built-in variables to populate the event data. For a comprehensive list of available variables and expressions, refer to the [Harness Variables and Expressions documentation](https://developer.harness.io/docs/platform/variables-and-expressions/harness-variables/#expression-paths).

Some key variables used in these templates include:

| Variable | Description |
|----------|-------------|
| `<+pipeline.executionId>` | Unique ID for the pipeline execution |
| `<+pipeline.identifier>` | Pipeline identifier |
| `<+pipeline.name>` | Pipeline name |
| `<+pipeline.triggeredBy.name>` | Name of the user who triggered the pipeline |
| `<+pipeline.triggeredBy.email>` | Email of the user who triggered the pipeline |
| `<+pipeline.execution.url>` | URL to the pipeline execution |
| `<+pipeline.startTs>` | Pipeline start unix timestamp |
| `<+pipeline.endTs>` | Pipeline end unix timestamp |
| `<+account.identifier>` | Harness account identifier |
| `<+org.identifier>` | Harness organization identifier |
| `<+project.identifier>` | Harness project identifier |

Also expression combinations like `<+pipeline.triggeredBy.email>/<+pipeline.triggeredBy.name>` can also be used which will result in `navneet.khandelwal@harness.io/Navneet` output. This can be helpful when pipeline is triggered by a cron expression where email is empty.

### Custom Variables

You can add custom variables to your templates using Harness custom variables. Refer to [Harness custom variable documentation](https://developer.harness.io/docs/platform/variables-and-expressions/add-a-variable/). This is useful for customer-specific constants like:

- `context.version`: Specifies the CDEvents version being used
- `context.type`: Defines the specific event type

These constants can then be referenced in your notification templates to customize the CDEvents output according to your specific requirements.

For examples of how these variables are populated with actual values, refer to the [examples](./conformance/) directory.

## Validation and Troubleshooting

To validate that your CDEvents notifications are correctly formatted:

1. Trigger a pipeline execution that will generate a notification
2. Check the webhook receiver logs to verify the JSON payload
3. Use a JSON validator to validate the payload against the CDEvents schema
   - CDEvents schemas are available at: [https://github.com/cdevents/spec/tree/main/schemas](https://github.com/cdevents/spec/tree/main/schemas)

## References

- [CDEvents Specification](https://cdevents.dev/)
- [CDEvents GitHub Repository](https://github.com/cdevents/spec)
- [Harness Custom Notification Templates](https://developer.harness.io/docs/platform/templates/customized-notification-template/)
- [CDEvents Schemas](https://github.com/cdevents/spec/tree/main/schemas)
