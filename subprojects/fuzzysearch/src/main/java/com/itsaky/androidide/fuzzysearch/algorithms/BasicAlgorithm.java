package com.itsaky.androidide.fuzzysearch.algorithms;

import com.itsaky.androidide.fuzzysearch.Applicable;
import com.itsaky.androidide.fuzzysearch.ToStringFunction;

public abstract class BasicAlgorithm implements Applicable {

  private ToStringFunction<String> stringFunction;

  public BasicAlgorithm() {
    this.stringFunction = new DefaultStringFunction();
  }

  public BasicAlgorithm(ToStringFunction<String> stringFunction) {
    this.stringFunction = stringFunction;
  }

  public int apply(String s1, String s2) {

    return apply(s1, s2, this.stringFunction);
  }

  public abstract int apply(String s1, String s2, ToStringFunction<String> stringProcessor);

  public BasicAlgorithm with(ToStringFunction<String> stringFunction) {
    setStringFunction(stringFunction);
    return this;
  }

  public BasicAlgorithm noProcessor() {
    this.stringFunction = ToStringFunction.NO_PROCESS;
    return this;
  }

  public ToStringFunction<String> getStringFunction() {
    return stringFunction;
  }

  void setStringFunction(ToStringFunction<String> stringFunction) {
    this.stringFunction = stringFunction;
  }
}
