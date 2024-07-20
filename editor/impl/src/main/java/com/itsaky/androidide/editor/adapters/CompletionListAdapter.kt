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
package com.itsaky.androidide.editor.adapters

import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.itsaky.androidide.editor.R
import com.itsaky.androidide.editor.databinding.LayoutCompletionItemBinding
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.java.utils.JavaType
import com.itsaky.androidide.lsp.models.ClassCompletionData
import com.itsaky.androidide.lsp.models.CompletionItemKind.CLASS
import com.itsaky.androidide.lsp.models.CompletionItemKind.CONSTRUCTOR
import com.itsaky.androidide.lsp.models.CompletionItemKind.ENUM
import com.itsaky.androidide.lsp.models.CompletionItemKind.FIELD
import com.itsaky.androidide.lsp.models.CompletionItemKind.INTERFACE
import com.itsaky.androidide.lsp.models.CompletionItemKind.METHOD
import com.itsaky.androidide.lsp.models.MemberCompletionData
import com.itsaky.androidide.lsp.models.MethodCompletionData
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.resources.R.string.msg_api_info_deprecated
import com.itsaky.androidide.resources.R.string.msg_api_info_removed
import com.itsaky.androidide.resources.R.string.msg_api_info_since
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.COMPLETION_WND_TEXT_API
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.COMPLETION_WND_TEXT_DETAIL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.COMPLETION_WND_TEXT_LABEL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.COMPLETION_WND_TEXT_TYPE
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.customOrJBMono
import com.itsaky.androidide.xml.versions.ApiVersions
import io.github.rosemoe.sora.widget.component.EditorCompletionAdapter
import org.eclipse.jdt.core.Signature
import com.itsaky.androidide.lsp.models.CompletionItem as LspCompletionItem

class CompletionListAdapter : EditorCompletionAdapter() {

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
    val binding =
      convertView?.let { LayoutCompletionItemBinding.bind(it) }
        ?: LayoutCompletionItemBinding.inflate(LayoutInflater.from(context), parent, false)
    val item = getItem(position) as LspCompletionItem
    val label = item.ideLabel
    val desc = item.detail
    var type: String? = item.completionKind.toString()
    val header = if (type!!.isEmpty()) "O" else type[0].toString()
    if (item.overrideTypeText != null) {
      type = item.overrideTypeText
    }
    binding.completionIconText.text = header
    binding.completionLabel.text = label
    binding.completionType.text = type
    binding.completionDetail.text = desc
    binding.completionIconText.setTypeface(
      customOrJBMono(EditorPreferences.useCustomFont),
      Typeface.BOLD
    )
    if (desc.isEmpty()) {
      binding.completionDetail.visibility = View.GONE
    }

    binding.completionApiInfo.visibility = View.GONE

    applyColorScheme(binding, isCurrentCursorPosition)
    showApiInfoIfNeeded(item, binding.completionApiInfo)
    return binding.root
  }

  private fun applyColorScheme(binding: LayoutCompletionItemBinding, isCurrent: Boolean) {
    setItemBackground(binding, isCurrent)
    var color = getThemeColor(COMPLETION_WND_TEXT_LABEL)
    if (color != 0) {
      binding.completionLabel.setTextColor(color)
      binding.completionIconText.setTextColor(color)
    }

    color = getThemeColor(COMPLETION_WND_TEXT_DETAIL)
    if (color != 0) {
      binding.completionDetail.setTextColor(color)
    }

    color = getThemeColor(COMPLETION_WND_TEXT_API)
    if (color != 0) {
      binding.completionApiInfo.setTextColor(color)
    }

    color = getThemeColor(COMPLETION_WND_TEXT_TYPE)
    if (color != 0) {
      binding.completionType.setTextColor(color)
    }
  }

  private fun setItemBackground(binding: LayoutCompletionItemBinding, isCurrent: Boolean) {
    val color =
      if (isCurrent) getThemeColor(SchemeAndroidIDE.COMPLETION_WND_BG_CURRENT_ITEM)
      else 0

    val cornerRadius = binding.root.context.resources
      .getDimensionPixelSize(R.dimen.completion_window_corner_radius).toFloat()

    val gd = GradientDrawable().apply {
      setColor(color)
      setCornerRadius(cornerRadius)
    }

    binding.root.background = gd
  }

  private fun showApiInfoIfNeeded(item: LspCompletionItem, textView: TextView) {
    executeAsync({
      if (!isValidForApiVersion(item)) {
        return@executeAsync null
      }

      val data = item.data
      val versions =
        Lookup.getDefault().lookup(ApiVersions.COMPLETION_LOOKUP_KEY) ?: return@executeAsync null
      val info =
        when (data) {
          is ClassCompletionData -> versions.classInfo(data.className)
          is MemberCompletionData -> {
            if (data is MethodCompletionData) {
              // if the member is a method
              // build the method identifier by joining the method name and the erased parameter types
              // for method 'int some(String)', the identifier becomes 'some(Ljava/lang/String;)'
              // return type of the method is ignored
              versions.memberInfo(
                data.classInfo.flatName,
                methodIdentifier(data.memberName, data.erasedParameterTypes)
              )
            } else {
              versions.memberInfo(data.classInfo.flatName, data.memberName)
            }
          }

          else -> return@executeAsync null
        }

      val sb = StringBuilder()
      if (info!!.since > 1) {
        sb.append(textView.context.getString(msg_api_info_since, info.since))
        sb.append("\n")
      }

      if (info.removedIn > 0) {
        sb.append(textView.context.getString(msg_api_info_removed, info.removedIn))
        sb.append("\n")
      }

      if (info.deprecatedIn > 0) {
        sb.append(textView.context.getString(msg_api_info_deprecated, info.deprecatedIn))
        sb.append("\n")
      }

      return@executeAsync sb
    }) {
      if (it.isNullOrBlank()) {
        textView.visibility = View.GONE
        return@executeAsync
      }

      textView.text = it
      textView.visibility = View.VISIBLE
    }
  }

  private fun methodIdentifier(memberName: String, erasedParameterTypes: List<String>): String {
    val sb = StringBuilder()
    sb.append(memberName)
    sb.append('(')
    for (type in erasedParameterTypes) {
      if (type.length == 1 && JavaType.primitiveFor(type[0]) != null) {
        sb.append(type)
      } else {
        sb.append(Signature.createTypeSignature(type, true))
      }
    }
    sb.append(')')
    return sb.toString()
  }

  private fun isValidForApiVersion(item: LspCompletionItem?): Boolean {
    if (item == null) {
      return false
    }
    val type = item.completionKind
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
      val className =
        when (data) {
          is ClassCompletionData -> data.className
          is MemberCompletionData -> data.classInfo.className
          else -> null
        }
      !className.isNullOrBlank()
    } else false
  }
}
