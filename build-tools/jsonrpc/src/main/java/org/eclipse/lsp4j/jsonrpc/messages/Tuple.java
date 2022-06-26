package org.eclipse.lsp4j.jsonrpc.messages;

/** Representation of tuple types. */
@SuppressWarnings("all")
public interface Tuple {

  public static <F, S> Two<F, S> two(F first, S second) {
    return new Two<F, S>(first, second);
  }

  /** A two-tuple, i.e. a pair. */
  public static class Two<F extends Object, S extends Object> implements Tuple {

    private final F first;
    private final S second;

    public Two(F first, S second) {
      this.first = first;
      this.second = second;
    }

    public F getFirst() {
      return this.first;
    }

    public S getSecond() {
      return this.second;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.first == null) ? 0 : this.first.hashCode());
      return prime * result + ((this.second == null) ? 0 : this.second.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Tuple.Two<?, ?> other = (Tuple.Two<?, ?>) obj;
      if (this.first == null) {
        if (other.first != null) return false;
      } else if (!this.first.equals(other.first)) return false;
      if (this.second == null) {
        if (other.second != null) return false;
      } else if (!this.second.equals(other.second)) return false;
      return true;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("Tuples.Two [").append(System.lineSeparator());
      builder.append("  first = ").append(first).append(System.lineSeparator());
      builder.append("  second = ").append(second).append(System.lineSeparator());
      return builder.append("]").toString();
    }
  }
}
