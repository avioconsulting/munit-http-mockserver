package com.avioconsulting.mule.mockserver.internal;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

@Xml(prefix = "http-mockserver")
@Extension(name = "HTTP MockServer")
@Configurations(MockServerConfiguration.class)
public class MockServerExtension {
}
