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

package com.itsaky.androidide.logging.encoder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import com.itsaky.androidide.logging.utils.LogUtils;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Encoder to format the log events similar to <i>logcat -v threadtime</i>.
 *
 * @author Akash Yadav
 */
public class ThreadTimeEncoder extends EncoderBase<ILoggingEvent> {

  @Override
  public byte[] headerBytes() {
    return new byte[0];
  }

  @Override
  public byte[] encode(ILoggingEvent event) {
    final var sb = new StringBuilder();
    final var date = new SimpleDateFormat("dd-MM HH:mm:ss.SSS", Locale.ROOT).format(
        new Date(event.getTimeStamp()));
    var tag = event.getLoggerName();
    if (tag.length() > LogUtils.MAX_TAG_LENGTH) {
      tag = tag.substring(0, LogUtils.MAX_TAG_LENGTH - 1) + "*";
    }

    sb.append(date);
    sb.append(' ');
    sb.append(String.format(Locale.ROOT, "%5d", pid()));
    sb.append(' ');
    sb.append(String.format(Locale.ROOT, "%5d", tid()));
    sb.append(' ');
    sb.append(event.getLevel().levelStr.charAt(0));
    sb.append(' ');
    sb.append(tag);
    sb.append(": ");
    sb.append(event.getFormattedMessage());

    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public byte[] footerBytes() {
    return new byte[0];
  }

  private long tid() {
    if (LogUtils.isJvm()) {
      return Thread.currentThread().getId();
    }

    return android.os.Process.myTid();
  }

  private long pid() {
    if (LogUtils.isJvm()) {
      // no public API to get process ID in Java
      return -1;
    }

    return android.os.Process.myUid();
  }
}
