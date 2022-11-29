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

package com.itsaky.androidide.inflater

/**
 * A listener which listens for XML layout inflation events.
 *
 * @author Akash Yadav
 */
interface IInflateEventsListener {

  /**
   * Called when an event occurs. The [event] parameter contains more information about the event.
   *
   * @param event The event.
   */
  fun onEvent(event: IInflationEvent<*>)

  /**
   * Allows the listener to intercept the view creation event and return view instance of their own.
   *
   * @param view The view that was created. The properties of this view is not expected to be
   * modified (see [OnInflateViewEvent] instead).
   * @return The view that should replace the [view] instance.
   */
  fun onInterceptCreateView(view: IView): IView = view
}

/**
 * An inflation event.
 *
 * @author Akash Yadav
 */
abstract class IInflationEvent<T>(val data: T)

/** Indicates start of layout inflation. */
class InflationStartEvent : IInflationEvent<Unit>(Unit)

/**
 * Indicates that the inflation has been finished.
 *
 * @property view The inflated view.
 */
class InflationFinishEvent(views: List<IView>) : IInflationEvent<List<IView>>(views)

/**
 * Indicates that the given view has been inflated. The view can be anywhere in the layout.
 *
 * @property view The inflated view.
 */
class OnInflateViewEvent(view: IView) : IInflationEvent<IView>(view)

/**
 * Indicates that the given [attribute] was added and applied to [view].
 *
 * @property view The view to which the attribute was applied.
 * @property attribute The attribute that was applied.
 */
class OnApplyAttributeEvent(view: IView, val attribute: IAttribute) : IInflationEvent<IView>(view)
