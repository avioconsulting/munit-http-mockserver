package com.avioconsulting.mule.mockserver.api.mock.model;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import java.io.InputStream;
import java.util.Map;

public class HttpRequest {

  @Parameter
  @ParameterDsl(allowReferences = false)
  private HttpConstants.Method method;

  @Parameter
  private String path;

  @Parameter
  @ParameterDsl(allowReferences = false)
  @NullSafe
  @Optional
  private Map<String, String> requestHeaders;

  @Parameter
  @ParameterDsl(allowReferences = false)
  @NullSafe
  @Alias("query-parameters")
  @Optional
  private Map<String, String> queryParameters;

  @Parameter
  @Optional
  @Content
  private TypedValue<InputStream> requestBody;

  public TypedValue<InputStream> getRequestBody() {
    return requestBody;
  }

  public HttpRequest setRequestBody(TypedValue<InputStream> requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public HttpConstants.Method getMethod() {
    return method;
  }

  public HttpRequest setMethod(HttpConstants.Method method) {
    this.method = method;
    return this;
  }

  public String getPath() {
    return path;
  }

  public HttpRequest setPath(String path) {
    this.path = path;
    return this;
  }

  public Map<String, String> getRequestHeaders() {
    return requestHeaders;
  }

  public Map<String, String> getQueryParameters() {
    return queryParameters;
  }

  public HttpRequest setQueryParameters(Map<String, String> queryParameters) {
    this.queryParameters = queryParameters;
    return this;
  }

  public HttpRequest setRequestHeaders(Map<String, String> requestHeaders) {
    this.requestHeaders = requestHeaders;
    return this;
  }
}
