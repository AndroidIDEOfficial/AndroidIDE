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

package com.itsaky.androidide.logsender.socket;

/**
 * Base class for socket commands.
 *
 * @author Akash Yadav
 */
public abstract class AbstractSocketCommand implements ISocketCommand {

  protected String[] getParams() {
    return null;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("/");
    sb.append(getName());

    final String[] params = getParams();
    if (params != null && params.length > 0) {
      sb.append(PARAM_DELIMITER);
      for (int i = 0; i < params.length; i++) {
        final String param = params[i];
        sb.append(param);
        if (i < params.length - 1) {
          sb.append(PARAM_DELIMITER);
        }
      }
    }

    return sb.toString();
  }
}
