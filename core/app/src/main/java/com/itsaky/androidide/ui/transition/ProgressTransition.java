/************************************************************************************
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
 **************************************************************************************/
package com.itsaky.androidide.ui.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import com.itsaky.androidide.utils.IntProperty;

public class ProgressTransition extends Transition {

  private static final String PROPNAME_PROGRESS = "ProgressTransition:progress";
  private static final Property<ProgressBar, Integer> PROGRESS_PROPERTY =
      new IntProperty<ProgressBar>(PROPNAME_PROGRESS) {

        @Override
        public void setValue(ProgressBar progressBar, int value) {
          progressBar.setProgress(value);
        }

        @Override
        public Integer get(ProgressBar progressBar) {
          return progressBar.getProgress();
        }
      };

  @Override
  public Animator createAnimator(
      ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
    if (startValues != null && endValues != null && endValues.view instanceof ProgressBar) {
      ProgressBar progressBar = (ProgressBar) endValues.view;
      int start = (Integer) startValues.values.get(PROPNAME_PROGRESS);
      int end = (Integer) endValues.values.get(PROPNAME_PROGRESS);
      if (start != end) {
        progressBar.setProgress(start);
        return ObjectAnimator.ofInt(progressBar, PROGRESS_PROPERTY, end);
      }
    }
    return null;
  }

  @Override
  public void captureStartValues(TransitionValues transitionValues) {
    captureValues(transitionValues);
  }

  @Override
  public void captureEndValues(TransitionValues transitionValues) {
    captureValues(transitionValues);
  }

  private void captureValues(TransitionValues transitionValues) {
    if (transitionValues.view instanceof ProgressBar) {
      ProgressBar progressBar = ((ProgressBar) transitionValues.view);
      transitionValues.values.put(PROPNAME_PROGRESS, progressBar.getProgress());
    }
  }
}
