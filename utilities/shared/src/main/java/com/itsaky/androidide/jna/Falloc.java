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

/* SPDX-License-Identifier: GPL-2.0 WITH Linux-syscall-note */

/**
 * <code>falloc.h</code>
 *
 * @author Akash Yadav
 */
public interface Falloc {

  /**
   * default is extend size
   */
  int FALLOC_FL_KEEP_SIZE = 0x01;
  /**
   * de-allocates range
   */
  int FALLOC_FL_PUNCH_HOLE = 0x02;
  /**
   * reserved codepoint
   */
  int FALLOC_FL_NO_HIDE_STALE = 0x04;

  /**
   * FALLOC_FL_COLLAPSE_RANGE is used to remove a range of a file without leaving a hole in the
   * file. The contents of the file beyond the range being removed is appended to the start offset
   * of the range being removed (i.e. the hole that was punched is "collapsed"), resulting in a file
   * layout that looks like the range that was removed never existed. As such collapsing a range of
   * a file changes the size of the file, reducing it by the same length of the range that has been
   * removed by the operation.
   * <p>
   * Different filesystems may implement different limitations on the granularity of the operation.
   * Most will limit operations to filesystem block size boundaries, but this boundary may be larger
   * or smaller depending on the filesystem and/or the configuration of the filesystem or file.
   * <p>
   * Attempting to collapse a range that crosses the end of the file is considered an illegal
   * operation - just use ftruncate(2) if you need to collapse a range that crosses EOF.
   */
  int FALLOC_FL_COLLAPSE_RANGE = 0x08;

  /**
   * FALLOC_FL_ZERO_RANGE is used to convert a range of file to zeros preferably without issuing
   * data IO. Blocks should be preallocated for the regions that span holes in the file, and the
   * entire range is preferable converted to unwritten extents - even though file system may choose
   * to zero out the extent or do whatever which will result in reading zeros from the range while
   * the range remains allocated for the file.
   * <p>
   * This can be also used to preallocate blocks past EOF in the same way as with fallocate. Flag
   * FALLOC_FL_KEEP_SIZE should cause the inode size to remain the same.
   */
  int FALLOC_FL_ZERO_RANGE = 0x10;

  /**
   * FALLOC_FL_INSERT_RANGE is use to insert space within the file size without overwriting any
   * existing data. The contents of the file beyond offset are shifted towards right by len bytes to
   * create a hole.  As such, this operation will increase the size of the file by len bytes.
   * <p>
   * Different filesystems may implement different limitations on the granularity of the operation.
   * Most will limit operations to filesystem block size boundaries, but this boundary may be larger
   * or smaller depending on the filesystem and/or the configuration of the filesystem or file.
   * <p>
   * Attempting to insert space using this flag at OR beyond the end of the file is considered an
   * illegal operation - just use ftruncate(2) or fallocate(2) with mode 0 for such type of
   * operations.
   */
  int FALLOC_FL_INSERT_RANGE = 0x20;

  /**
   * FALLOC_FL_UNSHARE_RANGE is used to unshare shared blocks within the file size without
   * overwriting any existing data. The purpose of this call is to preemptively reallocate any
   * blocks that are subject to copy-on-write.
   * <p>
   * Different filesystems may implement different limitations on the granularity of the operation.
   * Most will limit operations to filesystem block size boundaries, but this boundary may be larger
   * or smaller depending on the filesystem and/or the configuration of the filesystem or file.
   * <p>
   * This flag can only be used with allocate-mode fallocate, which is to say that it cannot be used
   * with the punch, zero, collapse, or insert range modes.
   */
  int FALLOC_FL_UNSHARE_RANGE = 0x40;
}
