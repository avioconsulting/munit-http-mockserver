package com.avioconsulting.mule.mockserver.internal;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.sdk.api.annotation.JavaVersionSupport;
import org.mule.sdk.api.meta.JavaVersion;

@Xml(prefix = "http-mockserver")
@Extension(name = "MUnit HTTP MockServer")
@Configurations(MockServerConfiguration.class)
@JavaVersionSupport({ JavaVersion.JAVA_8, JavaVersion.JAVA_11, JavaVersion.JAVA_17 })
public class MockServerExtension {
}
