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

import java.util.UUID;

/**
 * @author Akash Yadav
 */
public final class SocketCommandParser {

  public static ISocketCommand parse(String line) {
    // remove leading '/'
    line = line.substring(1);
    if (!line.contains(ISocketCommand.PARAM_DELIMITER)) {
      return create(line);
    }

    final String[] segments = line.split(ISocketCommand.PARAM_DELIMITER);
    if (segments.length >= 2) {
      return createParameterized(segments);
    }

    return null;
  }

  private static ISocketCommand create(String name) {
    if (name.equals(SignalCommand.STOP.getName())) {
      return SignalCommand.STOP;
    }

    return null;
  }

  private static ISocketCommand createParameterized(String[] segments) {
    final String command = segments[0];
    if (SenderInfoCommand.NAME.equals(command)) {
      try {
        // validate the sender ID
        // noinspection ResultOfMethodCallIgnored
        UUID.fromString(segments[1]);
      } catch (Exception e) {
        return null;
      }

      return new SenderInfoCommand(segments[1], segments[2]);
    }
    return null;
  }
}
