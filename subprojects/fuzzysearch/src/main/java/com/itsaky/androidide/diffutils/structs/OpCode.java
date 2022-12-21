package com.itsaky.androidide.diffutils.structs;

public final class OpCode {

  public EditType type;
  public int sbeg, send;
  public int dbeg, dend;

  @Override
  public String toString() {
    return type.name() + "(" + sbeg + "," + send + "," + dbeg + "," + dend + ")";
  }
}
