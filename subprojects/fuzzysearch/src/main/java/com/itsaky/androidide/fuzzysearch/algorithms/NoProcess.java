package com.itsaky.androidide.fuzzysearch.algorithms;

import com.itsaky.androidide.fuzzysearch.StringProcessor;

/**
 * @deprecated Use {@code ToStringFunction#NO_PROCESS} instead.
 */
@Deprecated
public class NoProcess extends StringProcessor {

  @Override
  @Deprecated
  public String process(String in) {
    return in;
  }
}
