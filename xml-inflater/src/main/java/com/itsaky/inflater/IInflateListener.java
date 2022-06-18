/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.inflater;

/**
 * A listener which is invoked to notify about events in {@link ILayoutInflater}
 *
 * @author Akash Yadav
 */
public interface IInflateListener {

  /** Called when the inflation process is about to start */
  void onBeginInflate();

  /**
   * Called when a view has been inflated
   *
   * @param view The view that will be inflated
   * @param parent The parent of this view
   */
  void onInflateView(IView view, IViewGroup parent);

  /**
   * Called when the attribute has been applied to the specified view
   *
   * @param attr The applied attribute
   * @param view The view to which attribute was applied
   */
  void onApplyAttribute(IAttribute attr, IView view);

  /**
   * Called when the inflation process finished successfully
   *
   * @param The root rootView of the inflated layout
   */
  void onFinishInflate(IView rootView);
}
