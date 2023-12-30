package com.avioconsulting.mule.mockserver;

import org.junit.Rule;
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

public class MockServerTests extends MuleArtifactFunctionalTestCase {

  @Rule
  public DynamicPort httpRequestPort = new DynamicPort("http.request.port");

  @Override
  protected String getConfigFile() {
    return "mule-http-mockserver-demo.xml";
  }

  @Test
  public void testVerifyAtLeast() {
    flowRunner("mule-http-mockserver-test-verify-at-least");
  }
}
