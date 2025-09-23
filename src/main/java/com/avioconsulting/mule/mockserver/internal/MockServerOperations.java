package com.avioconsulting.mule.mockserver.internal;

import java.io.InputStream;

import com.avioconsulting.mule.mockserver.api.mock.model.HttpRequest;
import com.avioconsulting.mule.mockserver.api.mock.model.HttpResponse;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import com.avioconsulting.mule.mockserver.api.mock.VerificationMethod;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

public class MockServerOperations {

  @Alias("set-expectation")
  public void setExpectation(@Connection MockServerConnection mockServer,
      @Content TypedValue<InputStream> expectation) {
    mockServer.createExpectation(expectation);
  }

  /**
   * Creates an expectation on the MockServer instance based on the provided
   * configuration.
   *
   * @param mockServer
   *            the MockServer connection used to create the expectation
   * @param matchRequest {@link HttpRequest} configuration for matching incoming requests
   * @param respondWith {@link HttpResponse} configuration for the response to return on match
   */
  @Alias("create-expectation")
  public void createExpectation(@Connection MockServerConnection mockServer,
      String expectationId,
      @ParameterGroup(name = "Match Request", showInDsl = true) @ParameterDsl(allowReferences = false) HttpRequest matchRequest,
      @ParameterGroup(name = "Respond with", showInDsl = true) @ParameterDsl(allowReferences = false) HttpResponse respondWith) {
    mockServer.createExpectation(expectationId, matchRequest, respondWith);
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
