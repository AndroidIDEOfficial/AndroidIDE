/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.adapters

import android.content.res.Resources
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.itsaky.androidide.resources.R.color
import com.itsaky.androidide.resources.R.string.msg_api_info_deprecated
import com.itsaky.androidide.resources.R.string.msg_api_info_removed
import com.itsaky.androidide.resources.R.string.msg_api_info_since
import com.itsaky.androidide.databinding.LayoutCompletionItemBinding
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem as LspCompletionItem
import com.itsaky.androidide.lsp.models.CompletionItemKind.CLASS
import com.itsaky.androidide.lsp.models.CompletionItemKind.CONSTRUCTOR
import com.itsaky.androidide.lsp.models.CompletionItemKind.ENUM
import com.itsaky.androidide.lsp.models.CompletionItemKind.FIELD
import com.itsaky.androidide.lsp.models.CompletionItemKind.INTERFACE
import com.itsaky.androidide.lsp.models.CompletionItemKind.METHOD
import com.itsaky.androidide.preferences.internal.useCustomFont
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.customOrJBMono
import com.itsaky.androidide.xml.versions.ApiVersions
import com.itsaky.androidide.xml.versions.Info
import io.github.rosemoe.sora.lang.completion.CompletionItem
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.component.EditorCompletionAdapter

class CompletionListAdapter : EditorCompletionAdapter() {
  override fun attachValues(window: EditorAutoCompletion, items: List<CompletionItem>) {
    super.attachValues(window, items)
  }

  override fun getItemHeight(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        40f,
        Resources.getSystem().displayMetrics
      )
      .toInt()
  }

  override fun getView(
    position: Int,
    convertView: View?,
    parent: ViewGroup?,
    isCurrentCursorPosition: Boolean,
  ): View {
    val binding = LayoutCompletionItemBinding.inflate(LayoutInflater.from(context), parent, false)
    val item = getItem(position) as LspCompletionItem
    val label = item.getLabel()
    val desc = item.detail
    var type: String? = item.kind.toString()
    val header = if (type!!.isEmpty()) "O" else type[0].toString()
    if (item.overrideTypeText != null) {
      type = item.overrideTypeText
    }
    binding.completionIconText.text = header
    binding.completionLabel.text = label
    binding.completionType.text = type
    binding.completionDetail.text = desc
    binding.completionIconText.setTypeface(customOrJBMono(useCustomFont), Typeface.BOLD)
    if (desc.isEmpty()) {
      binding.completionDetail.visibility = View.GONE
    }
    binding.root.setBackgroundColor(
      ContextCompat.getColor(
        context,
        if (isCurrentCursorPosition) color.completionList_backgroundSelected
        else color.completionList_background
      )
    )
    binding.completionApiInfo.visibility = View.GONE
    showApiInfoIfNeeded(item, binding.completionApiInfo)
    return binding.root
  }

  private fun showApiInfoIfNeeded(item: LspCompletionItem, textView: TextView) {
    executeAsync({
      if (!isValidForApiVersion(item)) {
        return@executeAsync null
      }

      val data = item.data
      if (data?.className == null) {
        return@executeAsync null
      }

      val versions =
        Lookup.DEFAULT.lookup(ApiVersions.COMPLETION_LOOKUP_KEY) ?: return@executeAsync null
      val className = data.className
      val kind = item.kind

      val clazz = versions.getClass(className) ?: return@executeAsync null
      var info: Info? = clazz

      if (
        kind == METHOD && data.erasedParameterTypes.isNotEmpty() && data.memberName.isNotBlank()
      ) {
        val method = clazz.getMethod(data.memberName, *data.erasedParameterTypes)
        if (method != null) {
          info = method
        }
      } else if (kind == FIELD && data.memberName.isNotBlank()) {
        val field = clazz.getField(data.memberName)
        if (field != null) {
          info = field
        }
      }

      val sb = StringBuilder()
      if (info!!.since > 1) {
        sb.append(textView.context.getString(msg_api_info_since, info.since))
        sb.append("\n")
      }

      if (info.removed > 0) {
        sb.append(textView.context.getString(msg_api_info_removed, info.removed))
        sb.append("\n")
      }

      if (info.deprecated > 0) {
        sb.append(textView.context.getString(msg_api_info_deprecated, info.deprecated))
        sb.append("\n")
      }

      return@executeAsync sb
    }) {
      if (it == null || it.isBlank()) {
        textView.visibility = View.GONE
        return@executeAsync
      }

      textView.text = it
      textView.visibility = View.VISIBLE
    }
  }

  private fun isValidForApiVersion(item: LspCompletionItem?): Boolean {
    if (item == null) {
      return false
    }
    val type = item.kind
    val data = item.data
    return if ( // These represent a class type
      (type === CLASS ||
        type === INTERFACE ||
        type === ENUM ||

        // These represent a method type
        type === METHOD ||
        type === CONSTRUCTOR ||

        // A field type
        type === FIELD) && data != null
    ) {
      !TextUtils.isEmpty(data.className)
    } else false
  }
}
