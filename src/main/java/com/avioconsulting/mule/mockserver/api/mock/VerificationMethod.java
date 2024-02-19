package com.avioconsulting.mule.mockserver.api.mock;

import java.util.function.Function;

import org.mockserver.verify.VerificationTimes;

public enum VerificationMethod {
  AT_LEAST(VerificationTimes::atLeast), AT_MOST(VerificationTimes::atMost), EXACTLY(VerificationTimes::exactly);

  private final Function<Integer, VerificationTimes> timesFunction;

  VerificationMethod(Function<Integer, VerificationTimes> timesFunction) {
    this.timesFunction = timesFunction;
  }

  public VerificationTimes times(int count) {
    return timesFunction.apply(count);
  }
}
