package com.itsaky.androidide.fuzzysearch.algorithms;

final class PrimitiveUtils {

  static double max(double... elems) {

    if (elems.length == 0) return 0;

    double best = elems[0];

    for (double t : elems) {
      if (t > best) {
        best = t;
      }
    }

    return best;
  }

  static int max(int... elems) {

    if (elems.length == 0) return 0;

    int best = elems[0];

    for (int t : elems) {
      if (t > best) {
        best = t;
      }
    }

    return best;
  }
}
