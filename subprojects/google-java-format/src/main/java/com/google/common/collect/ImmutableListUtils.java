/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */


package com.google.common.collect;

import java.util.Iterator;
import java.util.stream.Collector;

/**
 * @author Akash Yadav
 */
public class ImmutableListUtils {

  private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST =
      Collector.of(
          ImmutableList::builder,
          ImmutableList.Builder::add,
          ImmutableList.Builder::combine,
          ImmutableList.Builder::build);

  private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET =
      Collector.of(
          ImmutableSet::builder,
          ImmutableSet.Builder::add,
          ImmutableSet.Builder::combine,
          ImmutableSet.Builder::build);

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
    return (Collector) TO_IMMUTABLE_LIST;
  }

  // Sets

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
    return (Collector) TO_IMMUTABLE_SET;
  }

  /**
   * Returns an immutable list containing the given elements, in order.
   *
   * @throws NullPointerException if {@code elements} contains a null element
   */
  public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
    // We special-case for 0 or 1 elements, but going further is madness.
    if (!elements.hasNext()) {
      return ImmutableList.of();
    }
    E first = elements.next();
    if (!elements.hasNext()) {
      return ImmutableList.of(first);
    } else {
      return new ImmutableList.Builder<E>().add(first).addAll(elements).build();
    }
  }
}
