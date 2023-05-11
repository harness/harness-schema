# Copyright 2020 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

"""
Utility to dump changed file information in a PR to a file.
Very specific use case for collecting test case data for CI
for Harness jobs

Usage:
python get_pr.py <pull_request_id> <output_file> <access-token>
"""

import json
import sys
from urllib2 import urlopen, Request

FIELDS_TO_KEEP = ["filename", "status", "raw_url"]
OWNER = "wings-software"
REPO = "portal"

def get_changed_files(owner, repo, id, token):
  github_url = 'https://api.github.com/repos'
  url = "%s/%s/%s/pulls/%s/files" % (github_url, owner, repo, id)
  request = Request(url)
  request.add_header('Authorization', 'token %s' % token)
  return urlopen(request).read()

if __name__ == "__main__":
  pull_request_id = sys.argv[1]
  output_file = sys.argv[2]
  access_token = sys.argv[3]
  resp = get_changed_files(OWNER, REPO, pull_request_id, access_token)
  updated_resp = []
  for file_info in json.loads(resp):
    filtered_data = {}
    for field in FIELDS_TO_KEEP:
      filtered_data[field] = file_info[field]
    updated_resp.append(filtered_data)
  with open(output_file, "w") as f:
    f.write(json.dumps(updated_resp))
