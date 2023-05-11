/*
 * Copyright 2020 Harness Inc. All rights reserved.
 * Use of this source code is governed by the PolyForm Free Trial 1.0.0 license
 * that can be found in the licenses directory at the root of this repository, also available at
 * https://polyformproject.org/wp-content/uploads/2020/05/PolyForm-Free-Trial-1.0.0.txt.
 */

package io.harness.checks;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Slf4j
public class ReportProcessor {
  private final Set<String> flakyTests;

  public ReportProcessor(Set<String> flakyTests) {
    this.flakyTests = flakyTests;
  }

  private boolean isFlakyTest(String testFqdn) {
    return flakyTests.contains(testFqdn);
  }

  private NodeList skipped(Document document) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    String skippedXpath = "/testsuite/testcase[skipped]";
    return (NodeList) xPath.compile(skippedXpath).evaluate(document, XPathConstants.NODESET);
  }

  private NodeList failures(Document document) throws XPathExpressionException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    String failuresXpath = "/testsuite/testcase[error|failure]";
    return (NodeList) xPath.compile(failuresXpath).evaluate(document, XPathConstants.NODESET);
  }

  private Document readDocument(String filePath) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(filePath);
  }

  private void writeDocument(String filePath, Document doc) throws TransformerException {
    File fXmlFile = new File(filePath);
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(fXmlFile);
    transformer.transform(source, result);
  }

  public boolean removeFlakyTestsAndCheckSuccess(String filePath)
      throws ParserConfigurationException, IOException, SAXException, TransformerException, XPathExpressionException {
    Document doc = readDocument(filePath);
    NodeList testsuiteNodeList = doc.getElementsByTagName("testsuite");
    assert testsuiteNodeList.getLength() == 1;
    Node testsuiteNode = testsuiteNodeList.item(0);
    NodeList failuresNodeList = failures(doc);
    boolean success = true;
    for (int i = 0; i < failuresNodeList.getLength(); i++) {
      Node failureNode = failuresNodeList.item(i);
      String className = failureNode.getAttributes().getNamedItem("classname").getTextContent();
      String methodName = failureNode.getAttributes().getNamedItem("name").getTextContent();
      String testFqdn = className + "." + methodName;
      if (isFlakyTest(testFqdn)) {
        log.info("Suppressing failure for flaky test {}", testFqdn);
        testsuiteNode.removeChild(failureNode);
      } else {
        success = false;
      }
    }

    NodeList skippedNodeList = skipped(doc);
    for (int i = 0; i < skippedNodeList.getLength(); i++) {
      Node skippedNode = skippedNodeList.item(i);
      testsuiteNode.removeChild(skippedNode);
    }

    // TODO: update the overal file statistics

    writeDocument(filePath, doc);
    return success;
  }

  public int getFailureCount(String filePath)
      throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
    Document doc = readDocument(filePath);
    NodeList failuresNodeList = failures(doc);
    return failuresNodeList.getLength();
  }
}
