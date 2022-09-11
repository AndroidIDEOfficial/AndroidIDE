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

const val TRANSITION = "Transition"
const val TRANSITION_FADE = "Fade"
const val TRANSITION_SLIDE = "Slide"
const val TRANSITION_VISIBILITY = "VisibilityTransition"
const val TRANSITION_TARGET = "TransitionTarget"
const val TRANSITION_SET = "TransitionSet"
const val TRANSITION_CHANGE_TRANSFORM = "ChangeTransform"
const val TRANSITION_CHANGE_BOUNDS = "ChangeBounds"
const val TRANSITION_MANAGER = "TransitionManager"
const val TRANSITION_ARC_MOTION = "ArcMotion"
const val TRANSITION_PATTERN_PATH_MOTION = "PatternPathMotion"

fun forTransitionAttr(entry: String) : List<String> {
  return when(entry) {
    TRANSITION_SLIDE,
    TRANSITION_FADE -> mutableListOf(TRANSITION_VISIBILITY)
    else -> mutableListOf()
  }.apply {
    add(TRANSITION)
  }
}