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

/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.math.LongMath;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Akash Yadav
 * @see <a
 * href="https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Streams.java">Streams.java</a>
 */
public class Streams {

  public static <T extends Object> Stream<T> concat(Stream<? extends T>... streams) {
    // TODO(lowasser): consider an implementation that can support SUBSIZED
    boolean isParallel = false;
    int characteristics = Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL;
    long estimatedSize = 0L;
    ImmutableList.Builder<Spliterator<? extends T>> splitrsBuilder =
        new ImmutableList.Builder<>(streams.length);
    for (Stream<? extends T> stream : streams) {
      isParallel |= stream.isParallel();
      Spliterator<? extends T> splitr = stream.spliterator();
      splitrsBuilder.add(splitr);
      characteristics &= splitr.characteristics();
      estimatedSize = LongMath.saturatedAdd(estimatedSize, splitr.estimateSize());
    }
    return StreamSupport.stream(
            CollectSpliteratorUtils.flatMap(
                splitrsBuilder.build().spliterator(),
                splitr -> (Spliterator<T>) splitr,
                characteristics,
                estimatedSize),
            isParallel)
        .onClose(() -> closeAll(streams));
  }

  private static void closeAll(BaseStream<?, ?>[] toClose) {
    // If one of the streams throws a RuntimeException, continue closing the others, then throw the
    // exception later. If more than one stream throws an exception, the later ones are added to the
    // first as suppressed exceptions. We don't catch Error on the grounds that it should be allowed
    // to propagate immediately.
    RuntimeException exception = null;
    for (BaseStream<?, ?> stream : toClose) {
      try {
        stream.close();
      } catch (RuntimeException e) {
        if (exception == null) {
          exception = e;
        } else {
          exception.addSuppressed(e);
        }
      }
    }
    if (exception != null) {
      throw exception;
    }
  }

  /**
   * Returns a stream consisting of the results of applying the given function to the elements of
   * {@code stream} and their indices in the stream. For example,
   *
   * <pre>{@code
   * mapWithIndex(
   *     Stream.of("a", "b", "c"),
   *     (e, index) -> index + ":" + e)
   * }</pre>
   *
   * <p>would return {@code Stream.of("0:a", "1:b", "2:c")}.
   *
   * <p>The resulting stream is <a
   * href="http://gee.cs.oswego.edu/dl/html/StreamParallelGuidance.html">efficiently splittable</a>
   * if and only if {@code stream} was efficiently splittable and its underlying spliterator
   * reported {@link Spliterator#SUBSIZED}. This is generally the case if the underlying stream
   * comes from a data structure supporting efficient indexed random access, typically an array or
   * list.
   *
   * <p>The order of the resulting stream is defined if and only if the order of the original
   * stream was defined.
   */
  public static <T extends Object, R extends Object> Stream<R> mapWithIndex(
      Stream<T> stream, FunctionWithIndex<? super T, ? extends R> function) {
    checkNotNull(stream);
    checkNotNull(function);
    boolean isParallel = stream.isParallel();
    Spliterator<T> fromSpliterator = stream.spliterator();

    if (!fromSpliterator.hasCharacteristics(Spliterator.SUBSIZED)) {
      Iterator<T> fromIterator = Spliterators.iterator(fromSpliterator);
      return StreamSupport.stream(
              new AbstractSpliterator<R>(
                  fromSpliterator.estimateSize(),
                  fromSpliterator.characteristics() & (Spliterator.ORDERED | Spliterator.SIZED)) {
                long index = 0;

                @Override
                public boolean tryAdvance(Consumer<? super R> action) {
                  if (fromIterator.hasNext()) {
                    action.accept(function.apply(fromIterator.next(), index++));
                    return true;
                  }
                  return false;
                }
              },
              isParallel)
          .onClose(stream::close);
    }
    class Splitr extends MapWithIndexSpliterator<Spliterator<T>, R, Splitr> implements Consumer<T> {

      T holder;

      Splitr(Spliterator<T> splitr, long index) {
        super(splitr, index);
      }

      @Override
      public void accept(@ParametricNullness T t) {
        this.holder = t;
      }

      @Override
      public boolean tryAdvance(Consumer<? super R> action) {
        if (fromSpliterator.tryAdvance(this)) {
          try {
            // The cast is safe because tryAdvance puts a T into `holder`.
            action.accept(function.apply((T) holder, index++));
            return true;
          } finally {
            holder = null;
          }
        }
        return false;
      }

      @Override
      Splitr createSplit(Spliterator<T> from, long i) {
        return new Splitr(from, i);
      }
    }
    return StreamSupport.stream(new Splitr(fromSpliterator, 0), isParallel).onClose(stream::close);
  }

  private abstract static class MapWithIndexSpliterator<
      F extends Spliterator<?>,
      R extends Object,
      S extends MapWithIndexSpliterator<F, R, S>>
      implements Spliterator<R> {

    final F fromSpliterator;
    long index;

    MapWithIndexSpliterator(F fromSpliterator, long index) {
      this.fromSpliterator = fromSpliterator;
      this.index = index;
    }

    abstract S createSplit(F from, long i);

    @Override
    public S trySplit() {
      Spliterator<?> splitOrNull = fromSpliterator.trySplit();
      if (splitOrNull == null) {
        return null;
      }
      @SuppressWarnings("unchecked")
      F split = (F) splitOrNull;
      S result = createSplit(split, index);
      this.index += split.getExactSizeIfKnown();
      return result;
    }

    @Override
    public long estimateSize() {
      return fromSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
      return fromSpliterator.characteristics()
          & (Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
    }
  }

  public interface FunctionWithIndex<T extends Object, R extends Object> {

    /**
     * Applies this function to the given argument and its index within a stream.
     */
    @ParametricNullness
    R apply(@ParametricNullness T from, long index);
  }
}
