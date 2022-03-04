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

package com.itsaky.inflater.impl;

import com.itsaky.inflater.IView;

/**
 * Layout model for <strong>&lt;include&gt;</strong> tags.
 *
 * @author Akash Yadav
 */
public class IncludeLayout extends BaseView {

  public static final String TAG = "include";
  private final IView view;

  public IncludeLayout(IView view) {
    super(TAG, view.asView(), false);
    this.view = view;
  }

  @Override
  public String getXmlTag() {
    return TAG;
  }
}
