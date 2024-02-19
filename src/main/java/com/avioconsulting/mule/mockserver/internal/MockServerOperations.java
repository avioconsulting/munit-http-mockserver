package com.avioconsulting.mule.mockserver.internal;

import java.io.InputStream;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import com.avioconsulting.mule.mockserver.api.mock.VerificationMethod;

public class MockServerOperations {

  @Alias("set-expectation")
  public void setExpectation(@Connection MockServerConnection mockServer,
      @Content TypedValue<InputStream> expectation) {
    mockServer.setExpectation(expectation);
  }

  @Alias("verify-expectation")
  public void verifyExpectation(@Connection MockServerConnection mockServer, String expectationId,
      VerificationMethod comparison, Integer count) {
    mockServer.verifyExpectation(expectationId, comparison, count);
  }

  @Alias("clear-expectation-id")
  @Summary("clear all expectations and recorded requests matching provided expectation id")
  public void clearExpectationById(@Connection MockServerConnection mockServer, String expectationId) {
    mockServer.clearExpectation(expectationId);
  }

  @Alias("clear-expectation")
  @Summary("clear all expectations and recorded requests matching provided matcher")
  public void clearExpectationByMatcher(@Connection MockServerConnection mockServer,
      @Content TypedValue<InputStream> matcher) {
    mockServer.clearExpectation(matcher);
  }

  @Alias("reset-expectations")
  @Summary("clears all expectations and recorded requests")
  public void resetExpectations(@Connection MockServerConnection mockServer) {
    mockServer.resetExpectations();
  }
}
