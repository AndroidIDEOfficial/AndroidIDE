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

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TagTransformsTest {

  @Test
  fun `test drawable tag transforms`() {
    val tagMap =
      mapOf(
        "selector" to "StateListDrawable",
        "animated-selector" to "AnimatedStateListDrawable",
        "animation" to "AnimationDrawable",
        "shape" to "GradientDrawable",
        "corners" to "DrawableCorners",
        "layer" to "LayerDrawable",
        "level-list" to "LevelListDrawable",
        "rotate" to "RotateDrawable",
        "animated-rotate" to "AnimatedRotateDrawable",
        "inset" to "InsetDrawable",
        "bitmap" to "BitmapDrawable",
        "nine-patch" to "NinePatchDrawable",
        "adaptive-icon" to "AdaptiveIconDrawable",
        "ripple" to "RippleDrawable",
        "scale" to "ScaleDrawable",
        "clip" to "ClipDrawable",
        "vector" to "VectorDrawable",
        "animated-vector" to "AnimatedVectorDrawable"
      )

    tagMap.forEach { (tag, entry) ->
      assertThat(DrawableTagTransformer.transform(tag, "#document")).isEqualTo(entry)
    }
  }

  @Test
  fun `test drawable sub tag transforms`() {
    val tags =
      listOf(
        Triple("item", "selector", "StateListDrawableItem"),
        Triple("item", "animated-selector", "AnimatedStateListDrawableItem"),
        Triple("transition", "animated-selector", "AnimatedStateListDrawableTransition"),
        Triple("size", "shape", "GradientDrawableSize"),
        Triple("padding", "shape", "GradientDrawablePadding"),
        Triple("gradient", "shape", "GradientDrawableGradient")
      )

    tags.forEach {
      assertThat(DrawableTagTransformer.transform(it.first, it.second)).isEqualTo(it.third)
    }
  }

  @Test
  fun `test animation tag transforms`() {
    val tagMap =
      mapOf(
        "set" to "AnimationSet",
        "rotate" to "RotateAnimation",
        "scale" to "ScaleAnimation",
        "gridLayoutAnimation" to "GridLayoutAnimation",
        "accelerateInterpolator" to "AccelerateInterpolator",
        "accelerateDecelerateInterpolator" to "AccelerateDecelerateInterpolator"
      )
    
    tagMap.forEach { (tag, entry) ->
      assertThat(AnimTagTransformer.transform(tag)).isEqualTo(entry)
    }
  }
}
