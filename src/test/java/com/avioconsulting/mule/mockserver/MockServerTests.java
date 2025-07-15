package com.avioconsulting.mule.mockserver;

import org.junit.Rule;
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class MockServerTests extends MuleArtifactFunctionalTestCase {

  @Rule
  public DynamicPort httpsRequestPort = new DynamicPort("https.request.port");

  @Rule
  public DynamicPort httpRequestPort = new DynamicPort("http.request.port");

  @Override
  protected String[] getConfigFiles() {
    return new String[] { "mule-http-mockserver-demo.xml", "mule-https-mockserver-demo.xml" };
  }

  @Test
  public void testVerifyAtLeast() throws Exception {
    runFlow("mule-http-mockserver-test-verify-at-least");
  }

  @Test
  public void testVerifyHTTPSAtLeast() throws Exception {
    runFlow("mule-https-mockserver-test-verify-at-least");
  }
}
