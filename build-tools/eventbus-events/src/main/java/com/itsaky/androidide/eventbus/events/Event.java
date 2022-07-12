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

package com.itsaky.androidide.eventbus.events;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for Events. Additional properties that may be required by the event subscribers can be
 * specified using the {@link #put(Class, Object)} method and can be retrieved using {@link
 * #get(Class)} method.
 *
 * @author Akash Yadav
 */
public abstract class Event {

  private Map<Class<?>, Object> data = new HashMap<>();

  public <T> void put(Class<T> klass, T value) {
    data.put(klass, value);
  }

  public <T> T get(Class<T> klass) {
    return (T) data.get(klass);
  }
}
