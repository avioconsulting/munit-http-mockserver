package com.avioconsulting.mule.mockserver.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

@Operations(MockServerOperations.class)
@ConnectionProviders(MockServerConnectionProvider.class)
public class MockServerConfiguration {

}
