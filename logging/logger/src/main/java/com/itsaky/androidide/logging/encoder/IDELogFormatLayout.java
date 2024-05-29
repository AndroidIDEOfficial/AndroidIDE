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

import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log layout for AndroidIDE.
 *
 * @author Akash Yadav
 */
public class IDELogFormatLayout extends LayoutBase<ILoggingEvent> {

  private final Abbreviator loggerNameAbbreviator = new ClassNameOnlyAbbreviator();
  private boolean omitMessage = false;

  public void setOmitMessage(boolean omitMessage) {
    this.omitMessage = omitMessage;
  }

  public boolean isOmitMessage() {
    return omitMessage;
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    final SimpleDateFormat format = new SimpleDateFormat("dd-MM HH:mm:ss.SSS", Locale.ROOT);
    final var date = format.format(new Date(event.getTimeStamp()));

    final var builder = new StringBuilder();
    builder.append(date);
    builder.append(' ');
    builder.append(String.format(Locale.ROOT, "%5s", event.getLevel().levelStr));
    builder.append(' ');
    builder.append("[");
    builder.append(event.getThreadName());
    builder.append("]");
    builder.append(' ');
    builder.append(loggerNameAbbreviator.abbreviate(event.getLoggerName()));
    builder.append(": ");

    if (!isOmitMessage()) {
      builder.append(event.getFormattedMessage());
      builder.append(System.lineSeparator());
    }

    return builder.toString();
  }
}
