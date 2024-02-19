package com.avioconsulting.mule.mockserver.internal;

import javax.inject.Inject;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;

public class MockServerConnectionProvider implements CachedConnectionProvider<MockServerConnection> {

  @Parameter
  @DisplayName("Mockserver Properties file")
  @Optional(defaultValue = "mockserver.properties")
  private String propertiesFilePath;

  @Parameter
  @DisplayName("HTTP Port")
  private Integer port;

  @Inject
  private HttpService httpService;

  @RefName
  private String configName;

  public String getPropertiesFilePath() {
    return propertiesFilePath;
  }

  public MockServerConnectionProvider setPropertiesFilePath(String propertiesFilePath) {
    this.propertiesFilePath = propertiesFilePath;
    return this;
  }

  public Integer getPort() {
    return port;
  }

  public MockServerConnectionProvider setPort(Integer port) {
    this.port = port;
    return this;
  }

  @Override
  public MockServerConnection connect() throws ConnectionException {
    HttpClientConfiguration clientConfiguration = new HttpClientConfiguration.Builder().setName(configName).build();
    HttpClient httpClient = httpService.getClientFactory().create(clientConfiguration);
    return new MockServerConnection(getPropertiesFilePath(), getPort(), httpClient);
  }

  @Override
  public void disconnect(MockServerConnection connection) {
    connection.stop();
  }

  @Override
  public ConnectionValidationResult validate(MockServerConnection connection) {
    return connection.validate();
  }

}
