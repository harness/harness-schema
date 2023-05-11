# Copyright 2020 Harness Inc. All rights reserved.
# Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
# that can be found in the licenses directory at the root of this repository, also available at
# https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.

"""
Script to parse JUnit XML reports and write them to a CSV.
Usage:
    python xml_to_csv.py <regex for XML report paths> <output_file>
"""

from junitparser import (JUnitXml, TestSuite,
    TestCase, Skipped, Error, Failure)
import sys
import csv
import os, fnmatch
import sys
import glob

# Limit all error messages to 1000 characters
TEXT_LENGTH = 1000

class Status(object):
  SUCCESS = "SUCCESS"
  SKIPPED = "SKIPPED"
  ERROR = "ERROR"
  FAILURE = "FAILURE"

class XMLParser(object):
  def __init__(self, report_path):
    self.report_path = report_path
    self.parse_error_files = []

  def _find_files(self):
    for file in glob.glob(self.report_path):
        yield file

  @classmethod
  def parse_test_case(cls, test_case):
    """
    Returns tuple: (classname, test_name, time, status, err_message, err_description, err_type)
    """
    status = Status.SUCCESS
    err_description = err_msg = err_type = ""

    if isinstance(test_case.result, Skipped):
      status = Status.SKIPPED
      err_msg = str(test_case.result.message)
    if isinstance(test_case.result, Error):
      status = Status.ERROR
      err_type = test_case.result.type
      err_description = str(test_case.result._elem.text)
      err_msg = str(test_case.result.message)
    if isinstance(test_case.result, Failure):
      status = Status.FAILURE
      err_type = test_case.result.type
      err_description = str(test_case.result._elem.text)
      err_msg = str(test_case.result.message)

    return (test_case.classname, test_case.name, test_case.time,
            status, err_msg[:TEXT_LENGTH], err_description[:TEXT_LENGTH], err_type)

  def _get_test_cases(self, xml):
    """
    Get test cases in a single XML file. There are two formats
    which are used: Either all the testcases are present sequentialy
    without an outer tag or they are embodied in a testsuite tag.
    This handles both cases
    """
    for test_unit in xml:
      if isinstance(test_unit, TestSuite):
        for testcase in elem:
          yield test_case
      elif isinstance(test_unit, TestCase):
          yield test_unit

  def get_parsed_tests(self):
    for file in self._find_files():
      try:
        xml = JUnitXml.fromfile(file)
      except Exception as ex:
        self.parse_error_files.append(file)
        continue
      for test_case in self._get_test_cases(xml):
        test_info = XMLParser.parse_test_case(test_case)
        # Format: (class name, test case name, time taken, status, error description)
        yield test_info

if __name__ == "__main__":
  report_path = sys.argv[1]
  output_file = sys.argv[2]
  xml_parser = XMLParser(report_path)
  # Get parsed tests and write it to CSV
  with open(output_file, 'w') as csvfile:
    csv_writer = csv.writer(csvfile)
    csv_writer.writerow(("Classname", "Test name", "Time taken", "Status",
                         "Error message", "Error description", "Error Type"))
    for parsed_test in xml_parser.get_parsed_tests():
      csv_writer.writerow(parsed_test)
  print ("Done parsing and writing XMLs from %s to %s"  % (report_path, output_file))
  print ("Could not parse the following XML files: %s" % str(xml_parser.parse_error_files))
