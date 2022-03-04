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

package com.itsaky.inflater;

/**
 * Exception thrown by inflaters and drawable parsers to indicate that the a specific view or tag is
 * not supported.
 *
 * @author Akash Yadav
 */
public class NotSupportedException extends UnsupportedOperationException {

  public NotSupportedException() {}

  public NotSupportedException(String message) {
    super(message);
  }

  public NotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotSupportedException(Throwable cause) {
    super(cause);
  }
}
