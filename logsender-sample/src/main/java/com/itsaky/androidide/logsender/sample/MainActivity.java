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

package com.itsaky.androidide.logsender.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

  private final AtomicInteger count = new AtomicInteger(0);
  private final Handler handler = new Handler(Looper.getMainLooper());
  private final Runnable logger = new Runnable() {
    @Override
    public void run() {
      Log.d("LogSenderSample", "Log #" + count.getAndAdd(1));

      if (count.get() == 20) {
        return;
      }

      handler.postDelayed(this, 1000);
    }
  };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState
  ) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    handler.postDelayed(logger, 1000);

    findViewById(R.id.exception).setOnClickListener(v -> {
      throw new RuntimeException("Oops!");
    });

    findViewById(R.id.log_overflow).setOnClickListener(v -> new Thread(() -> {
      for (int i = 0; i < 100000; i++) {
        Log.d("ShouldNotBreak", "Log #" + i);
      }
    }).start());
  }
}
