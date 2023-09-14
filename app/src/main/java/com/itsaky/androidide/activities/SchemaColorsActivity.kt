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

package com.itsaky.androidide.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.ActivitySchemaColorsBinding
import com.itsaky.androidide.fragments.sheets.ProgressSheet

class SchemaColorsActivity : AppCompatActivity() {

  //i intend to move this activity to a specific folder for Popup Window

  private var _binding: ActivitySchemaColorsBinding? = null
  private val binding: ActivitySchemaColorsBinding
    get() = checkNotNull(_binding)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = ActivitySchemaColorsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupWindow()

    val intent = intent
    val action = intent.action
    val type = intent.type

    if (Intent.ACTION_VIEW == action && "application/zip" == type) {
      val progress = ProgressSheet()
      progress.setSubMessageEnabled(true)
      progress.setMessage("Please wait for a moment …")
      progress.setSubMessage("Importing color schemes ...")
      progress.isCancelable = false
      progress.show(supportFragmentManager,"importar_esquemas_de_cores" )

    }
  }

  fun setupWindow(){
    window.decorView.setBackgroundColor(Color.TRANSPARENT)
    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

  }
}