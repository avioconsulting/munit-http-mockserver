# HTTP MockServer Extension

HTTP MockServer Extension for MUnit.

This extension allows to use [MockServer](https://www.mock-server.com/#what-is-mockserver) in MUnit Testing for mocking and verifying HTTP invocations from the application flows.

## Installation

Add this dependency to your application pom.xml

```
<dependency>
    <groupId>com.avioconsulting.munit</groupId>
    <artifactId>munit-http-mockserver</artifactId>
    <version>${munit-http-mockserver.version}</version>
    <classifier>mule-plugin</classifier>
    <scope>test</scope>
</dependency>
```

See latest version on [Maven Central](https://central.sonatype.com/search?namespace=com.avioconsulting.munit&name=munit-http-mockserver).

## Usage

Consider a Mule app with following HTTP Request configuration and usage -

```xml

  <http:request-config name="HTTP_Request_configuration">
    <http:request-connection host="0.0.0.0" port="8081" />
  </http:request-config>

    <flow name="munit-http-mockserver-demoFlow">
        <http:request method="GET" config-ref="HTTP_Request_configuration" path="/api/info"/>
    </flow>

```

To use MockServer for the above request configuration, create a global configuration in your MUnit Test suite

```xml
  <http-mockserver:config name="HTTP_MockServer_Config">
    <http-mockserver:connection port="8081" />
  </http-mockserver:config>
```

For advanced configuration of the MockServer, [system properties approach](https://www.mock-server.com/mock_server/configuration_properties.html) can be used to define a custom `src/test/resources/mockserver.properties` file.

The following test uses the above configuration to set an expectation and verification using module operations

```xml
<munit:test name="http-mock-valid-expectation-test" doc:id="faf60afd-0a61-415f-aab0-3f0565e49432" description="Set Valid expectation">
    <munit:behavior >
      <http-mockserver:set-expectation config-ref="HTTP_MockServer_Config">
        <http-mockserver:expectation ><![CDATA[#[output application/json
---
{
  "id": "valid-expectation-id-1",
  "httpRequest" : {
    "method" : "GET",
    "path" : "/api/info"
  },
  "httpResponse" : {
    "body" : "some_response_body",
    "statusCode": 201
  }
}]]]></http-mockserver:expectation>
      </http-mockserver:set-expectation>
    </munit:behavior>
    <munit:execution>
      <flow-ref name="munit-http-mockserver-demoFlow"/>
    </munit:execution>
    <munit:validation>
      <munit-tools:assert-equals actual="#[attributes.statusCode]" expected="#[201]"/>
      <http-mockserver:verify-expectation comparison="AT_LEAST" config-ref="HTTP_MockServer_Config" expectationId="valid-expectation-id-1" count="1"/>
    </munit:validation>
  </munit:test>
```

To reduce the HTTP logging from MockServer, you may set `org.mockserver.log.MockServerEventLog` category to `WARN`.

### HTTPS Support

MockServer uses port unification to simplify the configuration so all protocols (i.e. HTTP, HTTPS / SSL, SOCKS, etc) are supported on the same port. This means when a request is sent over TLS (i.e. an HTTPS request) MockServer dynamically detects that the request is encrypted. See [MockServer HTTPS & TLS](https://mock-server.com/mock_server/HTTPS_TLS.html) for more details.

The MockServer in this module is configured to dynamically create CA certificates and private key that is saved to `./target/mockserver-ssl` directory in local.

Thus, the following configuration supports receiving HTTP as well as HTTPS inbound requests on 8081.

```xml
  <http-mockserver:config name="HTTP_MockServer_Config">
    <http-mockserver:connection port="8081" />
  </http-mockserver:config>
```

## Resources
See [modules tests](./src/test/munit/) for more examples and  [connector documentation](./docs/1.0.x/munit-http-mockserver-documentation.adoc) for supported operations.

Resources to learn about MockServer usage -
- https://github.com/mock-server/mockserver/blob/master/mockserver-examples/json_examples.md
