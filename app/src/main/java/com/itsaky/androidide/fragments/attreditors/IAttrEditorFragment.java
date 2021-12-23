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

package com.itsaky.androidide.fragments.attreditors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutBaseAttrEditorBinding;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.androidide.utils.Logger;

/**
 * Base class for every attribute editor.
 *
 * @author Akash Yadav
 */
public abstract class IAttrEditorFragment extends Fragment {
    
    private XMLAttribute attribute;
    
    protected static final String ATTRIBUTE_KEY = "xml_attribute";
    protected final Logger LOG = Logger.instance (getClass ().getSimpleName ());
    
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutBaseAttrEditorBinding binding = LayoutBaseAttrEditorBinding.inflate (inflater, container, false);
        binding.getRoot ().addView (createEditorView (inflater, binding.getRoot ()), generateEditorLayoutParams ());
        return binding.getRoot ();
    }
    
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
    
        if (getArguments () == null) {
            LOG.error ("No arguments found. Use IAttrEditorFragment#newInstance(..) to create these type of fragments");
            return;
        }
        
        final var args = getArguments ();
        final var attribute = (XMLAttribute) args.getParcelable (ATTRIBUTE_KEY);
    
        if (attribute == null) {
            throw new IllegalArgumentException ("How can we edit a null attribute?");
        }
        
        validate (attribute);
        
        this.attribute = attribute;
    }
    
    protected ConstraintLayout.LayoutParams generateEditorLayoutParams () {
        final var params = new ConstraintLayout.LayoutParams (0, 0);
        params.topToBottom = R.id.editor_divider;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        return params;
    }
    
    /**
     * Check if the provided attribute is valid for this editor or not.
     * If not, an {@link ValidationException} will be thrown.
     *
     * @param attribute The attribute to validate.
     * @return {@code true} if the attribute is valid, {@code false} otherwise.
     */
    protected boolean validate (XMLAttribute attribute) {
        final var attrs = StudioApp.getInstance ().attrInfo ();
        final var attr = attrs.getAttribute (attribute.getAttributeName ());
        
        if (attr == null) {
            throw new ValidationException ("Cannot find attribute with the given name: " + attribute.getAttributeName ());
        }
        
        for (var format : getSupportedFormats ()) {
            if ((attr.format & format) != 0) {
                LOG.error ("Error validating given attribute", "attr: " + attr, "attribute: " + attribute);
                throw new ValidationException ();
            }
        }
    }
    
    /**
     * Subclasses are supposed to inflate the layout in this method.
     *
     * @param inflater The layout inflater. Same as passed in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param parent The parent group to use when inflating the layout.
     * @return The created or inflated attribute editor view.
     */
    protected abstract View createEditorView (LayoutInflater inflater, ViewGroup parent);
    
    /**
     * Create a new instance of this attribute editor fragment.
     * TODO Instead of defining this in every subclass, can we use reflection to create instances by default?
     * How would it affect performance?
     * As of now, we only set arguments for the newly created fragment. So we can implement any generic method.
     *
     * @param attribute The attribute to be edited.
     * @return The new instance of this fragment.
     */
    protected abstract IAttrEditorFragment newInstance (XMLAttribute attribute);
    
    /**
     * Get the formats supported by this attribute editor.
     * This will be used to valid the attribute passed to this editor fragment.
     * in {@link #validate(XMLAttribute)}.
     *
     * @return The supported formats.
     */
    protected abstract int[] getSupportedFormats ();
    
    /**
     * An unchecked exception which is thrown if we fail to validate
     * the given attribute.
     */
    protected static class ValidationException extends RuntimeException {
        
        public ValidationException () {
            this("Given attribute is invalid.");
        }
        
        public ValidationException (String msg) {
            super(msg);
        }
    }
}
