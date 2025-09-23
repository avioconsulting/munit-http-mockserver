# HTTP MockServer Extension

<img src="./icon/icon.svg" width="200" height="200" alt="HTTP MockServer">

HTTP MockServer Extension for MUnit


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

The following tests use the above mock configuration to set expectations and verify using module operations.

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

<munit:test name="http-mockserver-test-create-expectation-with-payload" doc:id="352537f8-b597-497e-bf8e-4a0e1eb85abc" description="Use create expectations to mock">
<munit:behavior >
    <http-mockserver:create-expectation doc:name="Create expectation" doc:id="dac5bc23-ff50-4f05-9bb5-52399c510b16" config-ref="HTTP_MockServer_Config" expectationId="valid-expectation-id-1">
        <http-mockserver:match-request path="/api/info" method="POST">
            <http-mockserver:request-body ><![CDATA[#[output application/xml
---
command: 'greet']]]></http-mockserver:request-body>
        </http-mockserver:match-request>
        <http-mockserver:respond-with >
            <http-mockserver:response-body ><![CDATA[#[output application/json
---
{'say':'hello'}]]]></http-mockserver:response-body>
        </http-mockserver:respond-with>
    </http-mockserver:create-expectation>
</munit:behavior>
<munit:execution >
    <set-payload value="#[output application/xml --- command: 'greet']" doc:name="Set Payload" doc:id="00431632-e146-45d4-81ea-dd3434b796aa" />
    <http:request method="POST" doc:name="Request" doc:id="1602b9a6-8f56-4922-9f3f-e8271181a9d1" config-ref="HTTP_Request_configuration" path="/api/info"/>
</munit:execution>
<munit:validation >
    <http-mockserver:verify-expectation comparison="EXACTLY" doc:name="Verify expectation" doc:id="88f31758-5e01-4cce-b771-ebd5c7d2f294" config-ref="HTTP_MockServer_Config" count="1" expectationId="valid-expectation-id-1"/>
    <munit-tools:assert-equals doc:name="Assert equals" doc:id="d3382aa6-2ad2-4b67-9305-0b4033d0a6ee" actual="#[payload]" expected="#[output application/json --- {'say': 'hello'}]" message="Response payload does not match"/>
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
