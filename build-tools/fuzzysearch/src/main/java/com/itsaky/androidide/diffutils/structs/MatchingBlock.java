package com.itsaky.androidide.diffutils.structs;

public final class MatchingBlock {
  public int spos;
  public int dpos;
  public int length;

  @Override
  public String toString() {
    return "(" + spos + "," + dpos + "," + length + ")";
  }
}
