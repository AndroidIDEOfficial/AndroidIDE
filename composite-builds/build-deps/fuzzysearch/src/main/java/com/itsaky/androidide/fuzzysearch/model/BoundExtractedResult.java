package com.itsaky.androidide.fuzzysearch.model;

public class BoundExtractedResult<T> implements Comparable<BoundExtractedResult<T>> {

  private T referent;
  private String string;
  private int score;
  private int index;

  public BoundExtractedResult(T referent, String string, int score, int index) {
    this.referent = referent;
    this.string = string;
    this.score = score;
    this.index = index;
  }

  public T getReferent() {
    return referent;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "(string: " + string + ", score: " + score + ", index: " + index + ")";
  }

  @Override
  public int compareTo(BoundExtractedResult<T> o) {
    return Integer.compare(this.getScore(), o.getScore());
  }

  public int getScore() {
    return score;
  }
}
