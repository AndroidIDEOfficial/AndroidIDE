package com.itsaky.androidide.utils;

import android.text.Editable;
import android.text.TextWatcher;

/** Utility class if you want to only override one method of {@link TextWatcher} */
public class SingleTextWatcher implements TextWatcher {

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {}
}
