package com.avioconsulting.mule.mockserver.api.mock.model;

import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpResponse {

  @Parameter
  @Optional(defaultValue = "200")
  private int statusCode;

  @Parameter
  @Optional
  private String reasonPhrase;

  @Parameter
  @ParameterDsl(allowReferences = false)
  @NullSafe
  @Optional
  private Map<String, String> responseHeaders;

  @Parameter
  @Optional
  @Content
  @Alias("response-body")
  private TypedValue<InputStream> responseBody;

  @Parameter
  @Optional(defaultValue = "0")
  private int delayTime;

  @Parameter
  @Optional(defaultValue = "MILLISECONDS")
  private TimeUnit delayTimeUnit = TimeUnit.MILLISECONDS;

  public HttpResponse() {
  }

  public int getStatusCode() {
    return statusCode;
  }

  public HttpResponse setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  public HttpResponse setReasonPhrase(String reasonPhrase) {
    this.reasonPhrase = reasonPhrase;
    return this;
  }

  public Map<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  public HttpResponse setResponseHeaders(Map<String, String> responseHeaders) {
    this.responseHeaders = responseHeaders;
    return this;
  }

  public TypedValue<InputStream> getResponseBody() {
    return responseBody;
  }

  public HttpResponse setResponseBody(TypedValue<InputStream> responseBody) {
    this.responseBody = responseBody;
    return this;
  }

  public int getDelayTime() {
    return delayTime;
  }

  public HttpResponse setDelayTime(int delayTime) {
    this.delayTime = delayTime;
    return this;
  }

  public TimeUnit getDelayTimeUnit() {
    return delayTimeUnit;
  }

  public HttpResponse setDelayTimeUnit(TimeUnit delayTimeUnit) {
    this.delayTimeUnit = delayTimeUnit;
    return this;
  }
}
