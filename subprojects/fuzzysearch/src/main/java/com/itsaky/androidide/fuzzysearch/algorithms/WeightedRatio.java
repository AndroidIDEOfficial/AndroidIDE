package com.itsaky.androidide.fuzzysearch.algorithms;

import static com.itsaky.androidide.fuzzysearch.FuzzySearch.partialRatio;
import static com.itsaky.androidide.fuzzysearch.FuzzySearch.ratio;
import static com.itsaky.androidide.fuzzysearch.FuzzySearch.tokenSetPartialRatio;
import static com.itsaky.androidide.fuzzysearch.FuzzySearch.tokenSetRatio;
import static com.itsaky.androidide.fuzzysearch.FuzzySearch.tokenSortPartialRatio;
import static com.itsaky.androidide.fuzzysearch.FuzzySearch.tokenSortRatio;
import static com.itsaky.androidide.fuzzysearch.algorithms.PrimitiveUtils.max;
import static java.lang.Math.round;

import com.itsaky.androidide.fuzzysearch.ToStringFunction;

@SuppressWarnings("WeakerAccess")
public class WeightedRatio extends BasicAlgorithm {

  public static final double UNBASE_SCALE = .95;
  public static final double PARTIAL_SCALE = .90;
  public static final boolean TRY_PARTIALS = true;

  @Override
  public int apply(String s1, String s2, ToStringFunction<String> stringProcessor) {

    s1 = stringProcessor.apply(s1);
    s2 = stringProcessor.apply(s2);

    int len1 = s1.length();
    int len2 = s2.length();

    if (len1 == 0 || len2 == 0) {
      return 0;
    }

    boolean tryPartials = TRY_PARTIALS;
    double unbaseScale = UNBASE_SCALE;
    double partialScale = PARTIAL_SCALE;

    int base = ratio(s1, s2);
    double lenRatio = ((double) Math.max(len1, len2)) / Math.min(len1, len2);

    // if strings are similar length don't use partials
    if (lenRatio < 1.5) tryPartials = false;

    // if one string is much shorter than the other
    if (lenRatio > 8) partialScale = .6;

    if (tryPartials) {

      double partial = partialRatio(s1, s2) * partialScale;
      double partialSor = tokenSortPartialRatio(s1, s2) * unbaseScale * partialScale;
      double partialSet = tokenSetPartialRatio(s1, s2) * unbaseScale * partialScale;

      return (int) round(max(base, partial, partialSor, partialSet));

    } else {

      double tokenSort = tokenSortRatio(s1, s2) * unbaseScale;
      double tokenSet = tokenSetRatio(s1, s2) * unbaseScale;

      return (int) round(max(base, tokenSort, tokenSet));
    }
  }
}
