#!/bin/bash

# Define list1 and list2 as arrays
list1=(
  "aqua-trivy-scan-node.yaml"
  "aws-ecr-scan-node.yaml"
  "aws-security-hub-scan-node.yaml"
  "background-step-node.yaml"
  "bandit-scan-node.yaml"
  "black-duck-scan-node.yaml"
  "brakeman-scan-node.yaml"
  "burp-scan-node.yaml"
  "checkmarx-scan-node.yaml"
  "clair-scan-node.yaml"
  "codeql-scan-node.yaml"
  "custom-ingest-scan-node.yaml"
  "data-theorem-scan-node.yaml"
  "docker-content-trust-scan-node.yaml"
  "fortify-on-demand-scan-node.yaml"
  "fossa-scan-node.yaml"
  "gitleaks-scan-node.yaml"
  "grype-scan-node.yaml"
  "jfrog-xray-scan-node.yaml"
  "mend-scan-node.yaml"
  "metasploit-scan-node.yaml"
  "nessus-scan-node.yaml"
  "nexus-iqscan-node.yaml"
  "nikto-scan-node.yaml"
  "nmap-scan-node.yaml"
  "openvas-scan-node.yaml"
  "owasp-scan-node.yaml"
  "plugin-step-node.yaml"
  "prisma-cloud-scan-node.yaml"
  "prowler-scan-node.yaml"
  "qualys-scan-node.yaml"
  "reapsaw-scan-node.yaml"
  "run-step-node.yaml"
  "security-node.yaml"
  "semgrep-scan-node.yaml"
  "shift-left-scan-node.yaml"
  "sniper-scan-node.yaml"
  "snyk-scan-node.yaml"
  "sonarqube-scan-node.yaml"
  "sysdig-scan-node.yaml"
  "tenable-scan-node.yaml"
  "veracode-scan-node.yaml"
  "zap-scan-node.yaml"
  "policy-step-node.yaml"
  "template-step-node.yaml"
)


list2=(
  "asg-blue-green-deploy-step-node.yaml"
  "asg-blue-green-rollback-step-node.yaml"
  "asg-blue-green-swap-service-step-node.yaml"
  "asg-canary-delete-step-node.yaml"
  "asg-canary-deploy-step-node.yaml"
  "asg-rolling-deploy-step-node.yaml"
  "asg-rolling-rollback-step-node.yaml"
  "aws-lambda-deploy-step-node.yaml"
  "aws-lambda-rollback-step-node.yaml"
  "aws-sam-deploy-step-node.yaml"
  "aws-sam-rollback-step-node.yaml"
  "azure-a-r-m-rollback-step-node.yaml"
  "azure-create-a-r-m-resource-step-node.yaml"
  "azure-create-b-p-step-node.yaml"
  "azure-web-app-rollback-step-node.yaml"
  "azure-web-app-slot-deployment-step-node.yaml"
  "azure-web-app-swap-slot-step-node.yaml"
  "azure-web-app-traffic-shift-step-node.yaml"
  "bamboo-build-step-node.yaml"
  "chaos-step-node.yaml"
  "cloudformation-create-stack-step-node.yaml"
  "cloudformation-delete-stack-step-node.yaml"
  "cloudformation-rollback-step-node.yaml"
  "command-step-node.yaml"
  "create-p-r-step-node.yaml"
  "ecs-blue-green-create-service-step-node.yaml"
  "ecs-blue-green-rollback-step-node.yaml"
  "ecs-blue-green-swap-target-groups-step-node.yaml"
  "ecs-canary-delete-step-node.yaml"
  "ecs-canary-deploy-step-node.yaml"
  "ecs-rolling-deploy-step-node.yaml"
  "ecs-rolling-rollback-step-node.yaml"
  "ecs-run-task-step-node.yaml"
  "elastigroup-b-g-stage-setup-step-node.yaml"
  "elastigroup-deploy-step-node.yaml"
  "elastigroup-rollback-step-node.yaml"
  "elastigroup-setup-step-node.yaml"
  "elastigroup-swap-route-step-node.yaml"
  "fetch-instance-script-step-node.yaml"
  "fetch-linked-apps-step-node.yaml"
  "google-functions-deploy-step-node.yaml"
  "google-functions-deploy-without-traffic-step-node.yaml"
  "google-functions-rollback-step-node.yaml"
  "google-functions-traffic-shift-step-node.yaml"
  "helm-deploy-step-node.yaml"
  "helm-rollback-step-node.yaml"
  "jenkins-build-step-node.yaml"
  "k8s-apply-step-node.yaml"
  "k8s-b-g-swap-services-step-node.yaml"
  "k8s-blue-green-step-node.yaml"
  "k8s-canary-delete-step-node.yaml"
  "k8s-canary-step-node.yaml"
  "k8s-delete-step-node.yaml"
  "k8s-dry-run-manifest-step-node.yaml"
  "k8s-rolling-rollback-step-node.yaml"
  "k8s-rolling-step-node.yaml"
  "k8s-scale-step-node.yaml"
  "merge-pr-step-node.yaml"
  "revert-pr-step-node.yaml"
  "serverless-aws-lambda-deploy-step-node.yaml"
  "serverless-aws-lambda-deploy-v2-step-node.yaml"
  "serverless-aws-lambda-package-v2-step-node.yaml"
  "serverless-aws-lambda-prepare-rollback-v2-step-node.yaml"
  "serverless-aws-lambda-rollback-step-node.yaml"
  "serverless-aws-lambda-rollback-v2-step-node.yaml"
  "shell-script-provision-step-node.yaml"
  "sync-step-node.yaml"
  "tas-app-resize-step-node.yaml"
  "tas-b-g-app-setup-step-node.yaml"
  "tas-basic-app-setup-step-node.yaml"
  "tas-canary-app-setup-step-node.yaml"
  "tas-command-step-node.yaml"
  "tas-rollback-step-node.yaml"
  "tas-rolling-deploy-step-node.yaml"
  "tas-rolling-rollback-step-node.yaml"
  "tas-swap-rollback-step-node.yaml"
  "tas-swap-routes-step-node.yaml"
  "terraform-apply-step-node.yaml"
  "terraform-cloud-rollback-step-node.yaml"
  "terraform-cloud-run-step-node.yaml"
  "terraform-destroy-step-node.yaml"
  "terraform-plan-step-node.yaml"
  "terraform-rollback-step-node.yaml"
  "terragrunt-apply-step-node.yaml"
  "terragrunt-destroy-step-node.yaml"
  "terragrunt-plan-step-node.yaml"
  "terragrunt-rollback-step-node.yaml"
  "update-release-repo-step-node.yaml"
  "artifactory-upload-node.yaml"
  "bitrise-step-node.yaml"
  "build-and-push-acrnode.yaml"
  "build-and-push-docker-node.yaml"
  "build-and-push-ecrnode.yaml"
  "build-and-push-gcrnode.yaml"
  "gcsupload-node.yaml"
  "git-clone-step-node.yaml"
  "restore-cache-gcsnode.yaml"
  "restore-cache-s3-node.yaml"
  "run-test-step-node.yaml"
  "s3-upload-node.yaml"
  "save-cache-gcsnode.yaml"
  "save-cache-s3-node.yaml"
  "ssca-enforcement-step-node.yaml"
  "ssca-orchestration-step-node.yaml"
  "action-step-node.yaml"
  "aqua-trivy-scan-node.yaml"
  "aws-ecr-scan-node.yaml"
  "aws-security-hub-scan-node.yaml"
  "background-step-node.yaml"
  "bandit-scan-node.yaml"
  "black-duck-scan-node.yaml"
  "brakeman-scan-node.yaml"
  "burp-scan-node.yaml"
  "checkmarx-scan-node.yaml"
  "clair-scan-node.yaml"
  "codeql-scan-node.yaml"
  "custom-ingest-scan-node.yaml"
  "data-theorem-scan-node.yaml"
  "docker-content-trust-scan-node.yaml"
  "fortify-on-demand-scan-node.yaml"
  "fossa-scan-node.yaml"
  "gitleaks-scan-node.yaml"
  "grype-scan-node.yaml"
  "jfrog-xray-scan-node.yaml"
  "mend-scan-node.yaml"
  "metasploit-scan-node.yaml"
  "nessus-scan-node.yaml"
  "nexus-iqscan-node.yaml"
  "nikto-scan-node.yaml"
  "nmap-scan-node.yaml"
  "openvas-scan-node.yaml"
  "owasp-scan-node.yaml"
  "plugin-step-node.yaml"
  "prisma-cloud-scan-node.yaml"
  "prowler-scan-node.yaml"
  "qualys-scan-node.yaml"
  "reapsaw-scan-node.yaml"
  "run-step-node.yaml"
  "security-node.yaml"
  "semgrep-scan-node.yaml"
  "shift-left-scan-node.yaml"
  "sniper-scan-node.yaml"
  "snyk-scan-node.yaml"
  "sonarqube-scan-node.yaml"
  "sysdig-scan-node.yaml"
  "tenable-scan-node.yaml"
  "veracode-scan-node.yaml"
  "zap-scan-node.yaml"
  "barrier-step-node.yaml"
  "container-step-node.yaml"
  "custom-approval-step-node.yaml"
  "email-step-node.yaml"
  "flag-configuration-step-node.yaml"
  "harness-approval-step-node.yaml"
  "http-step-node.yaml"
  "jira-approval-step-node.yaml"
  "jira-create-step-node.yaml"
  "jira-update-step-node.yaml"
  "policy-step-node.yaml"
  "queue-step-node.yaml"
  "service-now-approval-step-node.yaml"
  "service-now-create-step-node.yaml"
  "service-now-import-set-step-node.yaml"
  "service-now-update-step-node.yaml"
  "shell-script-step-node.yaml"
  "template-step-node.yaml"
  "wait-step-node.yaml"
  "cv-analyze-deployment-step-node.yaml"
  "cv-verify-step-node.yaml"
  "iacmapproval-step-node.yaml"
  "iacmterraform-plugin-step-node.yaml"
)


# Loop through each element in list1
for element in "${list1[@]}"; do
    found=false

    # Loop through each element in list2
    for item in "${list2[@]}"; do
        if [[ "$element" == "$item" ]]; then
            found=true
            break
        fi
    done

    # If the element is not found in list2, print it
    if [ "$found" = false ]; then
        echo "$element"
    fi
done