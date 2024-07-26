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

package com.itsaky.androidide.jna;

import com.sun.jna.Native;

/**
 * @author Akash Yadav
 */
public interface LibC extends com.sun.jna.platform.linux.LibC {

  String NAME = "c";
  LibC INSTANCE = Native.load(NAME, LibC.class);

  /**
   * `fallocate` system call.
   *
   * @param fd     The file descriptor.
   * @param mode   Operation identifier. See {@link com.sun.jna.platform.linux.Fcntl Fcntl} for
   *               valid operation codes.
   * @param offset The offset in the file to operate on.
   * @param len    The len of the region to operate on.
   * @return The result of `fallocate`. See {@link com.sun.jna.platform.linux.Fcntl Fcntl} for more
   * details.
   */
  int fallocate(int fd, int mode, long offset, long len);
}
