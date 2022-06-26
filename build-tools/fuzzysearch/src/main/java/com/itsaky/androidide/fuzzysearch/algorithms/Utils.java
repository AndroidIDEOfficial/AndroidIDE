package com.itsaky.androidide.fuzzysearch.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public final class Utils {

  public static <T extends Comparable<T>> List<T> findTopKHeap(List<T> arr, int k) {
    PriorityQueue<T> pq = new PriorityQueue<T>();

    for (T x : arr) {
      if (pq.size() < k) pq.add(x);
      else if (x.compareTo(pq.peek()) > 0) {
        pq.poll();
        pq.add(x);
      }
    }
    List<T> res = new ArrayList<>();
    for (int i = k; i > 0; i--) {
      T polled = pq.poll();
      if (polled != null) {
        res.add(polled);
      }
    }
    return res;
  }

  static Set<String> tokenizeSet(String in) {

    return new HashSet<>(tokenize(in));
  }

  static List<String> tokenize(String in) {

    return Arrays.asList(in.split("\\s+"));
  }

  static String sortAndJoin(Set<String> col, String sep) {

    return sortAndJoin(new ArrayList<>(col), sep);
  }

  static String sortAndJoin(List<String> col, String sep) {

    Collections.sort(col);

    return join(col, sep);
  }

  static String join(List<String> strings, String sep) {
    final StringBuilder buf = new StringBuilder(strings.size() * 16);

    for (int i = 0; i < strings.size(); i++) {

      if (i < strings.size()) {
        buf.append(sep);
      }

      buf.append(strings.get(i));
    }

    return buf.toString().trim();
  }

  static <T extends Comparable<? super T>> T max(T... elems) {

    if (elems.length == 0) return null;

    T best = elems[0];

    for (T t : elems) {
      if (t.compareTo(best) > 0) {
        best = t;
      }
    }

    return best;
  }
}
