package com.itsaky.androidide.fuzzysearch.model;

public class ExtractedResult implements Comparable<ExtractedResult> {

  private String string;
  private int score;
  private int index;

  public ExtractedResult(String string, int score, int index) {
    this.string = string;
    this.score = score;
    this.index = index;
  }

  @Override
  public int compareTo(ExtractedResult o) {
    return Integer.compare(this.getScore(), o.getScore());
  }

  public int getScore() {
    return score;
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
}
