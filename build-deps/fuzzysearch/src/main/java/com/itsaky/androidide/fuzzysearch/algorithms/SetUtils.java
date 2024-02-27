package com.itsaky.androidide.fuzzysearch.algorithms;

import java.util.HashSet;
import java.util.Set;

final class SetUtils {

  static <T> Set<T> intersection(Set<T> s1, Set<T> s2) {

    Set<T> intersection = new HashSet<>(s1);
    intersection.retainAll(s2);

    return intersection;
  }

  static <T> Set<T> difference(Set<T> s1, Set<T> s2) {

    Set<T> difference = new HashSet<>(s1);
    difference.removeAll(s2);

    return difference;
  }
}
