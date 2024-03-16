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

package com.itsaky.androidide.lsp.java.utils;

import android.os.Handler;
import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * A timer which helps the language server to run a lint check after a specified interval.
 *
 * @author Akash Yadav
 */
public class AnalyzeTimer {

  public static final long DEFAULT_INTERVAL = 400;
  @NonNull private final Handler timerHandler;
  @NonNull private final Runnable timerCallback;
  private long interval;
  private boolean started;

  /**
   * Creates a new AnalyzeTimer instance.
   *
   * @param timerCallback The callback that will be invoked after the timer has ended.
   */
  public AnalyzeTimer(@NonNull Runnable timerCallback) {
    this.timerHandler = new Handler();
    this.interval = DEFAULT_INTERVAL;
    this.timerCallback = timerCallback;

    Objects.requireNonNull(this.timerCallback, "Callback cannot be null");
  }

  /**
   * Get the interval set to this timer.
   *
   * @return The interval.
   */
  public long getInterval() {
    return interval;
  }

  /**
   * Set the interval for the lint timer.
   *
   * @param interval The interval after which <code>timerCallback</code> will be called.
   */
  public void setInterval(long interval) {
    this.interval = interval;

    if (this.interval <= 0) {
      throw new IllegalArgumentException("Invalid interval specified for timer.");
    }
  }

  /** Starts the timer. */
  public void start() {
    restart();
  }

  /** Restarts the timer. */
  public void restart() {
    timerHandler.removeCallbacks(timerCallback);
    timerHandler.postDelayed(timerCallback, interval);
    started = true;
  }

  /** Shutdown the timer. Cancels any running timers. */
  public void cancel() {
    timerHandler.removeCallbacks(timerCallback);
    started = false;
  }

  public boolean isStarted() {
    return started;
  }
}
