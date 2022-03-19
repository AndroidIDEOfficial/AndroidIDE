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

package com.itsaky.androidide.fragments.attr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.inflater.IAttribute;
import java.util.Objects;

/**
 * Base class for value editor fragment.
 *
 * @author Akash Yadav
 */
public class BaseValueEditorFragment extends Fragment {

    protected OnValueChangeListener mValueChangeListener;
    protected XMLAttribute attribute;
    protected String name;

    public void setAttribute(XMLAttribute attribute) {
        this.attribute = attribute;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mValueChangeListener = listener;
    }

    protected void notifyValueChanged(@NonNull String newValue) {
        Objects.requireNonNull(newValue);

        if (mValueChangeListener != null) {
            mValueChangeListener.onValueChanged(this.attribute, newValue);
        }
    }

    /** Listener for listening for value change event. */
    public interface OnValueChangeListener {
        /**
         * Called when the value was changed.
         *
         * @param attribute The attribute whose value was changed.
         * @param newValue The new value for the editor.
         */
        void onValueChanged(IAttribute attribute, String newValue);
    }
}
