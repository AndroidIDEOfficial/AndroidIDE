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

package com.itsaky.androidide.lsp.xml.utils

import com.itsaky.androidide.lsp.xml.providers.completion.transformToEntryName

/** Transforms tag names to styleable entry names. */
interface ITagTransformer {
  /** Returns the styleable entry name for the given tag name and its parent's tag name. */
  fun transform(tag: String, parent: String = ""): String
}

internal object NoOpTagTransformer : ITagTransformer {
  override fun transform(tag: String, parent: String): String {
    return ""
  }
}

internal open class SimpleTagTransformer : ITagTransformer {
  override fun transform(tag: String, parent: String): String {
    return transformToEntryName(tag)
  }
}

internal object DrawableTagTransformer : ITagTransformer {
  override fun transform(tag: String, parent: String): String {
    if (tag == "corners") {
      return "DrawableCorners"
    }

    val prefix =
      if (parent == "item" || parent.isNotEmpty()) {
        toEntry(parent).run { if (startsWith('#')) toEntry(tag) else this }
      } else tag
    val suffix =
      if (parent.isNotEmpty() && !parent.startsWith('#')) transformToEntryName(tag) else ""

    return "${prefix}Drawable${suffix}"
  }
}

internal object AnimTagTransformer : ITagTransformer {
  override fun transform(tag: String, parent: String): String {
    if (tag == "set") {
      return "AnimationSet"
    }

    return "${toEntry(tag)}${animEntrySuffix(tag)}"
  }

  /** Returns the suffix that is required to get the styleable entry for an anim resource. */
  private fun animEntrySuffix(tag: String): String {
    return when {
      tag.endsWith("Animation") -> ""
      tag.endsWith("Interpolator") -> ""
      else -> "Animation"
    }
  }
}

internal object AnimatorTagTransformer : SimpleTagTransformer() {
  override fun transform(tag: String, parent: String): String {
    return toEntry(tag)
  }
}

internal object TransitionTagTransformer : SimpleTagTransformer() {
  override fun transform(tag: String, parent: String): String {
    if (tag == "target") {
      return TRANSITION_TARGET
    }
    return super.transform(tag, parent)
  }
}

internal object MenuTagTransformer : ITagTransformer {
  override fun transform(tag: String, parent: String): String {
    return transformToEntryName(tag, "Menu")
  }
}

/**
 * Convert the tag name to entry name.
 *
 * @param tag The tag name.
 */
private fun toEntry(tag: String): String {
  if (tag.startsWith('#')) {
    return tag
  }

  when (tag) {
    "selector" -> return "StateList"
    "animated-selector" -> return "AnimatedStateList"
    "shape" -> return "Gradient"
    "set" -> return "AnimatorSet"
    "keyframe" -> return "KeyFrame"
    "objectAnimator" -> return "PropertyAnimator"
  }

  return transformToEntryName(tag)
}
