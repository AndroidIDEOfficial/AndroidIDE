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

package com.itsaky.androidide.logsender.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.itsaky.androidide.logsender.sample.databinding.ActivityMainBinding
import java.util.concurrent.atomic.AtomicInteger

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding

  private val count = AtomicInteger(0)
  private val handler = Handler(Looper.getMainLooper())
  private val logger =
    object : Runnable {
      override fun run() {
        Log.d("LogSenderSample", "Log #${count.getAndAdd(1)}")
        if (count.get() == 20) {
          return
        }
        handler.postDelayed(this, 1000)
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    handler.postDelayed(logger, 1000)
  }
}
