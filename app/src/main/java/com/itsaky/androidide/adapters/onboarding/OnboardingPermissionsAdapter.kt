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

package com.itsaky.androidide.adapters.onboarding

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.button.MaterialButton
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.LayoutOnboardingPermissionItemBinding
import com.itsaky.androidide.models.OnboardingPermissionItem

/**
 * @author Akash Yadav
 */
class OnboardingPermissionsAdapter(private val permissions: List<OnboardingPermissionItem>,
  private val requestPermission: (String) -> Unit) :
  RecyclerView.Adapter<OnboardingPermissionsAdapter.ViewHolder>() {

  class ViewHolder(val binding: LayoutOnboardingPermissionItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutOnboardingPermissionItemBinding.inflate(LayoutInflater.from(parent.context), parent,
        false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val binding = holder.binding
    val permission = permissions[position]

    binding.infoContent.apply {
      title.setText(permission.title)
      description.setText(permission.description)
    }

    binding.grantButton.setOnClickListener {
      requestPermission(permission.permission)
    }

    if (permission.isGranted) {
      binding.grantButton.apply {
        isEnabled = false
        text = ""
        icon = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_ok)
        iconTint = ColorStateList.valueOf(
          ContextCompat.getColor(binding.root.context, R.color.green_500))
        iconGravity = MaterialButton.ICON_GRAVITY_TEXT_TOP
        iconPadding = 0
        iconSize = SizeUtils.dp2px(28f)
      }
    }
  }

  override fun getItemCount(): Int {
    return permissions.size
  }
}