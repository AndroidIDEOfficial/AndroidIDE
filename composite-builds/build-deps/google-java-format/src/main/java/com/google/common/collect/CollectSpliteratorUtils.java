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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.max;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Akash Yadav
 */
public class CollectSpliteratorUtils {

  /**
   * Returns a {@code Spliterator} that iterates over the elements of the spliterators generated by
   * applying {@code function} to the elements of {@code fromSpliterator}.
   */
  static <InElementT extends Object, OutElementT extends Object>
  Spliterator<OutElementT> flatMap(
      Spliterator<InElementT> fromSpliterator,
      Function<? super InElementT, Spliterator<OutElementT>> function,
      int topCharacteristics,
      long topSize) {
    checkArgument(
        (topCharacteristics & Spliterator.SUBSIZED) == 0,
        "flatMap does not support SUBSIZED characteristic");
    checkArgument(
        (topCharacteristics & Spliterator.SORTED) == 0,
        "flatMap does not support SORTED characteristic");
    checkNotNull(fromSpliterator);
    checkNotNull(function);
    return new FlatMapSpliteratorOfObject<>(
        null, fromSpliterator, function, topCharacteristics, topSize);
  }

  static final class FlatMapSpliteratorOfObject<
      InElementT extends Object, OutElementT extends Object>
      extends FlatMapSpliterator<InElementT, OutElementT, Spliterator<OutElementT>> {
    FlatMapSpliteratorOfObject(
        Spliterator<OutElementT> prefix,
        Spliterator<InElementT> from,
        Function<? super InElementT, Spliterator<OutElementT>> function,
        int characteristics,
        long estimatedSize) {
      super(
          prefix, from, function, FlatMapSpliteratorOfObject::new, characteristics, estimatedSize);
    }
  }

  abstract static class FlatMapSpliterator<
      InElementT extends Object,
      OutElementT extends Object,
      OutSpliteratorT extends Spliterator<OutElementT>>
      implements Spliterator<OutElementT> {
    /** Factory for constructing {@link FlatMapSpliterator} instances. */
    @FunctionalInterface
    interface Factory<InElementT extends Object, OutSpliteratorT extends Spliterator<?>> {
      OutSpliteratorT newFlatMapSpliterator(
          OutSpliteratorT prefix,
          Spliterator<InElementT> fromSplit,
          Function<? super InElementT, OutSpliteratorT> function,
          int splitCharacteristics,
          long estSplitSize);
    }

    OutSpliteratorT prefix;
    final Spliterator<InElementT> from;
    final Function<? super InElementT, OutSpliteratorT> function;
    final Factory<InElementT, OutSpliteratorT> factory;
    int characteristics;
    long estimatedSize;

    FlatMapSpliterator(
        OutSpliteratorT prefix,
        Spliterator<InElementT> from,
        Function<? super InElementT, OutSpliteratorT> function,
        Factory<InElementT, OutSpliteratorT> factory,
        int characteristics,
        long estimatedSize) {
      this.prefix = prefix;
      this.from = from;
      this.function = function;
      this.factory = factory;
      this.characteristics = characteristics;
      this.estimatedSize = estimatedSize;
    }

    /*
     * The tryAdvance and forEachRemaining in FlatMapSpliteratorOfPrimitive are overloads of these
     * methods, not overrides. They are annotated @Override because they implement methods from
     * Spliterator.OfPrimitive (and override default implementations from Spliterator.OfPrimitive or
     * a subtype like Spliterator.OfInt).
     */

    @Override
    public final boolean tryAdvance(Consumer<? super OutElementT> action) {
      while (true) {
        if (prefix != null && prefix.tryAdvance(action)) {
          if (estimatedSize != Long.MAX_VALUE) {
            estimatedSize--;
          }
          return true;
        } else {
          prefix = null;
        }
        if (!from.tryAdvance(fromElement -> prefix = function.apply(fromElement))) {
          return false;
        }
      }
    }

    @Override
    public final void forEachRemaining(Consumer<? super OutElementT> action) {
      if (prefix != null) {
        prefix.forEachRemaining(action);
        prefix = null;
      }
      from.forEachRemaining(
          fromElement -> {
            Spliterator<OutElementT> elements = function.apply(fromElement);
            if (elements != null) {
              elements.forEachRemaining(action);
            }
          });
      estimatedSize = 0;
    }

    @Override
    public final OutSpliteratorT trySplit() {
      Spliterator<InElementT> fromSplit = from.trySplit();
      if (fromSplit != null) {
        int splitCharacteristics = characteristics & ~Spliterator.SIZED;
        long estSplitSize = estimateSize();
        if (estSplitSize < Long.MAX_VALUE) {
          estSplitSize /= 2;
          this.estimatedSize -= estSplitSize;
          this.characteristics = splitCharacteristics;
        }
        OutSpliteratorT result =
            factory.newFlatMapSpliterator(
                this.prefix, fromSplit, function, splitCharacteristics, estSplitSize);
        this.prefix = null;
        return result;
      } else if (prefix != null) {
        OutSpliteratorT result = prefix;
        this.prefix = null;
        return result;
      } else {
        return null;
      }
    }

    @Override
    public final long estimateSize() {
      if (prefix != null) {
        estimatedSize = max(estimatedSize, prefix.estimateSize());
      }
      return max(estimatedSize, 0);
    }

    @Override
    public final int characteristics() {
      return characteristics;
    }
  }
}
