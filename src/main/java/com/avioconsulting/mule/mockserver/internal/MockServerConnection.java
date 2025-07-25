package com.avioconsulting.mule.mockserver.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.mockserver.configuration.Configuration;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.socket.tls.KeyStoreFactory;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.i18n.I18nMessageFactory;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.domain.HttpProtocol;
import org.mule.runtime.http.api.domain.entity.InputStreamHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import com.avioconsulting.mule.mockserver.api.mock.VerificationMethod;
import org.slf4j.event.Level;

import javax.net.ssl.HttpsURLConnection;

public class MockServerConnection {

  private final String propertiesFilePath;
  private final Integer port;
  private ClientAndServer clientAndServer;
  private final HttpClient httpClient;
  private String baseUri;

  public MockServerConnection(String propertiesFilePath, Integer port, HttpClient httpClient) {
    this.propertiesFilePath = propertiesFilePath;
    this.port = port;
    this.httpClient = httpClient;
    String julFilePath = System.getProperty("java.util.logging.config.file");
    if (julFilePath == null) {
      // This is to avoid @MockServerLogger#configureLogger setting
      // org.mockserver.logging.StandardOutConsoleHandler as a JUL handler.
      // Due to classpath isolation, this class from test dependency
      // is not visible at application and results in Class not found error
      System.setProperty("java.util.logging.config.file", "");
    }
    init();
  }

  public void stop() {
    if (clientAndServer != null && clientAndServer.hasStarted()) {
      clientAndServer.stop();
    }
    if (httpClient != null) {
      httpClient.stop();
    }
    System.clearProperty("mockserver.propertyFile");
  }

  private void init() {
    Objects.requireNonNull(propertiesFilePath, "properties file path must not be null");
    System.setProperty("mockserver.propertyFile", propertiesFilePath);
    Configuration configuration = Configuration.configuration()
        .disableSystemOut(true)
        .logLevel(Level.valueOf(System.getProperty("mockserver.logLevel", "INFO")))
        .dynamicallyCreateCertificateAuthorityCertificate(true)
        .directoryToSaveDynamicSSLCertificate("./target/mockserver-ssl/");
    HttpsURLConnection.setDefaultSSLSocketFactory(
        new KeyStoreFactory(configuration, new MockServerLogger()).sslContext().getSocketFactory());
    clientAndServer = ClientAndServer.startClientAndServer(configuration, port);
    boolean started = clientAndServer.hasStarted(5, 10, TimeUnit.SECONDS);
    if (!started)
      throw new MuleRuntimeException(I18nMessageFactory.createStaticMessage("MockServer did not start in time"));
    httpClient.start();
    baseUri = String.format("%s://%s:%d", "http", "localhost", clientAndServer.getPort());
  }

  public void setExpectation(TypedValue<InputStream> expectation) {
    sendRequest(expectation, "/mockserver/expectation");
  }

  public void verifyExpectation(String expectationId, VerificationMethod method, Integer count) {
    clientAndServer.verify(expectationId, method.times(count));
  }

  public void clearExpectation(String expectationId) {
    clientAndServer.clear(expectationId);
  }

  public void resetExpectations() {
    clientAndServer.reset();
  }

  public void clearExpectation(TypedValue<InputStream> expectation) {
    sendRequest(expectation, "/mockserver/clear");
  }

  private void sendRequest(TypedValue<InputStream> expectation, String path) {
    Objects.requireNonNull(expectation);
    Objects.requireNonNull(expectation.getValue());
    InputStreamHttpEntity entity = new InputStreamHttpEntity(expectation.getValue(), expectation.getByteLength());
    HttpRequest request = HttpRequest.builder().method(HttpConstants.Method.PUT).uri(baseUri + path)
        .protocol(HttpProtocol.HTTP_1_1).addHeader("Accepts", "application/json").entity(entity).build();
    try {
      HttpResponse httpResponse = httpClient.send(request);
      switch (httpResponse.getStatusCode()) {
        case 400:
          throw new MuleRuntimeException(I18nMessageFactory
              .createStaticMessage("Incorrect request format - %s", httpResponse.getReasonPhrase()));
        case 406:
          throw new MuleRuntimeException(I18nMessageFactory.createStaticMessage("Invalid expectation - %s",
              httpResponse.getReasonPhrase()));
        default:
          break;
      }
    } catch (IOException | TimeoutException e) {
      throw new MuleRuntimeException(e);
    }
  }

  public ConnectionValidationResult validate() {
    if (clientAndServer == null || !clientAndServer.hasStarted() || httpClient == null) {
      return ConnectionValidationResult.failure("MockServer is not running",
          new ConnectionException("MockServer is not running"));
    } else {
      return ConnectionValidationResult.success();
    }
  }
}
