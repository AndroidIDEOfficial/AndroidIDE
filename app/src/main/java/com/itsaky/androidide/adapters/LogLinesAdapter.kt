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

package com.itsaky.androidide.adapters

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.adapters.LogLinesAdapter.VH
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.TypefaceUtils

/**
 * Adapter for the log view.
 * @author Akash Yadav
 */
class LogLinesAdapter(private val lines: MutableList<LogLine> = mutableListOf()) :
    RecyclerView.Adapter<VH>() {

    var simpleFormatting = true

    companion object {
        const val MAX_LINES = 10 * 1000
    }

    class VH(val text: TextView) : RecyclerView.ViewHolder(text)

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        this.lines.clear()
        this.notifyDataSetChanged()
    }

    fun add(line: LogLine) {
        this.lines.add(line)
        this.notifyItemInserted(this.lines.lastIndex)

        if (this.lines.size > MAX_LINES) {
            this.lines.removeAt(0)
            this.notifyItemRemoved(0)
        }
    }

    fun getItem(index: Int) = this.lines[index]

    fun getAsString() =
        StringBuilder()
            .apply {
                for (line in lines) {
                    append(
                        if (simpleFormatting) {
                            line.toSimpleString()
                        } else {
                            line.toString()
                        })
                    append("\n")
                }
            }
            .toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val dp1 = SizeUtils.dp2px(1f)
        val text = TextView(parent.context)
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        text.setPaddingRelative(dp1, dp1 / 2, dp1, dp1 / 2)
        text.setTextIsSelectable(true)
        text.typeface = TypefaceUtils.jetbrainsMono()
        return VH(text)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val line = getItem(position)
        val spanned =
            if (line is LogWrapper) {
                line.spanned!!
            } else {
                val spannable =
                    SpannableString(
                        if (simpleFormatting) line.toSimpleString() else line.toString())
                val color =
                    when (line.priority) {
                        LogLine.ERROR -> -0xbbcca
                        LogLine.WARNING -> -0x14c5
                        LogLine.INFO -> -0xb350b0
                        else -> -0xa0a0b
                    }
                spannable.setSpan(
                    ForegroundColorSpan(color),
                    0,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                lines[position] = LogWrapper(line).apply { spanned = spannable }
                spannable
            }
        holder.text.text = spanned
    }

    override fun getItemCount() = this.lines.size

    class LogWrapper(val log: LogLine) : LogLine() {

        var spanned: Spannable? = null

        override fun equals(other: Any?): Boolean {
            return this.log == other
        }

        override fun hashCode(): Int {
            return this.log.hashCode()
        }

        override fun toString() = this.log.toString()
        override fun toSimpleString(): String = this.log.toSimpleString()
        override fun formattedTagAndMessage(): String = this.log.formattedTagAndMessage()
    }
}
