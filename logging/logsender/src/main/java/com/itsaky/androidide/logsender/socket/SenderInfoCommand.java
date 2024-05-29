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
 * Command containing information about the log sender.
 *
 * @author Akash Yadav
 */
public class SenderInfoCommand extends AbstractSocketCommand {

  public static final String NAME = "sender";

  public final String senderId, packageName;

  public SenderInfoCommand(String senderId, String packageName) {
    this.senderId = senderId;
    this.packageName = packageName;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  protected String[] getParams() {
    return new String[]{this.senderId, this.packageName};
  }
}
