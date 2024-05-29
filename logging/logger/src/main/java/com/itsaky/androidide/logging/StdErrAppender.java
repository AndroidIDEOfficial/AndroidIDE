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

package com.itsaky.androidide.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.itsaky.androidide.logging.encoder.ThreadTimeEncoder;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Akash Yadav
 */
public class StdErrAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

  private Encoder<ILoggingEvent> encoder = null;

  @Override
  public void start() {
    if (encoder == null) {
      addWarn("No encoder set for the appender named [" + name
          + "]. Falling back to ThreadTimeEncoder.");
      encoder = new ThreadTimeEncoder();
    }

    super.start();
  }

  @Override
  protected void append(ILoggingEvent eventObject) {
    if (!isStarted()) {
      return;
    }

    Objects.requireNonNull(encoder, "Encoder must not be null");
    byte[] bytes = encoder.encode(eventObject);
    try {
      System.err.write(bytes);
    } catch (IOException e) {
      addError("Failed to write to stderr", e);
    }
  }

  /**
   * Set the encoder for this appender.
   *
   * @param encoder The encoder.
   */
  public void setEncoder(
      Encoder<ILoggingEvent> encoder
  ) {
    this.encoder = encoder;
  }


  /**
   * Get the encoder for this appender.
   *
   * @return The encoder.
   */
  public Encoder<ILoggingEvent> getEncoder() {
    return encoder;
  }
}
