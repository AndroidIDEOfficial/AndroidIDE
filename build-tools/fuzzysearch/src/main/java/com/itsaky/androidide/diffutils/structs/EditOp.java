package com.itsaky.androidide.diffutils.structs;

public final class EditOp {

  public EditType type;
  public int spos; // source block pos
  public int dpos; // destination block pos

  @Override
  public String toString() {
    return type.name() + "(" + spos + "," + dpos + ")";
  }
}
