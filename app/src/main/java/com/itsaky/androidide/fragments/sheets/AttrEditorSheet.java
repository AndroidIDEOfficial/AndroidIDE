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

package com.itsaky.androidide.fragments.sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.AttributeListAdapter;
import com.itsaky.androidide.adapters.SimpleIconTextAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutAttrEditorSheetBinding;
import com.itsaky.androidide.databinding.LayoutAttrEditorSheetItemBinding;
import com.itsaky.androidide.models.IconTextListItem;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.layoutinflater.IView;
import com.itsaky.toaster.Toaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AttrEditorSheet extends BottomSheetDialogFragment implements SimpleIconTextAdapter.OnBindListener {
    
    private static final List<IconTextListItem> VIEW_ACTIONS = new ArrayList<> ();
    
    private IView selectedView;
    private LayoutAttrEditorSheetBinding binding;
    private final Logger LOG = Logger.instance ("AttrSheetEditor");
    
    private OnViewDeletionFailedListener mDeletionFailedListener;
    
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = LayoutAttrEditorSheetBinding.inflate (LayoutInflater.from (getContext ()), container, false);
        return binding.getRoot ();
    }
    
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        
        if (VIEW_ACTIONS.isEmpty ()) {
            Collections.addAll (VIEW_ACTIONS,
                    IconTextListItem.create (getString (R.string.title_viewaction_delete), R.drawable.ic_delete),
                    IconTextListItem.create (getString (R.string.msg_viewaction_select_parent), R.drawable.ic_view_select_parent)
            );
        }
        
        setupViewData ();
    }
    
    public AttrEditorSheet setDeletionFailedListener (OnViewDeletionFailedListener listener) {
        this.mDeletionFailedListener = listener;
        return this;
    }
    
    private void setupViewData () {
        binding.actionsList.setAdapter (new SimpleIconTextAdapter (VIEW_ACTIONS).setOnBindListener (this));
        
        if (this.selectedView == null) {
            LOG.error ("Cannot edit attributes of a null view.");
            return;
        }
        
        binding.widgetName.setText (this.selectedView.getXmlTag ());
        binding.attrList.setAdapter (new AttributeListAdapter (
                this.selectedView.getAttributes ()
                        .stream ()
                        .map (XMLAttribute::new)
                        .collect (Collectors.toList ()), this::onAttrClick)
        );
    }
    
    public void setSelectedView (IView view) {
        this.selectedView = view;
    }
    
    private void onAttrClick (LayoutAttrEditorSheetItemBinding binding, XMLAttribute attribute) {
    }
    
    @Override
    public void postBind (IconTextListItem item, @NonNull SimpleIconTextAdapter.VH holder, int position) {
        final var binding = holder.binding;
        binding.getRoot ().setOnClickListener (v -> onViewActionClick (item, holder, position));
    }
    
    private void onViewActionClick (@NonNull IconTextListItem item, SimpleIconTextAdapter.VH holder, int position) {
        if (this.selectedView == null) {
            return;
        }
        
        if (position == 0) { // Delete
            DialogUtils.newYesNoDialog (getContext (), (dialog, which) -> {
                var handled = selectedView.removeFromParent ();
                if (!handled) {
                    handled = mDeletionFailedListener != null && mDeletionFailedListener.onDeletionFailed (this.selectedView);
                }
                if (!handled) {
                    StudioApp.getInstance ().toast (getString(R.string.msg_view_deletion_failed), Toaster.Type.ERROR);
                } else {
                    dismiss ();
                }
            }, (dialog, which) -> {
                dialog.dismiss ();
            }).show ();
        } else if (position == 1) { // Select parent
            if (this.selectedView.getParent () == null) {
                StudioApp.getInstance ().toast (getString (R.string.msg_no_view_parent), Toaster.Type.ERROR);
                return;
            }
            
            this.selectedView = this.selectedView.getParent ();
            
            TransitionManager.beginDelayedTransition (binding.getRoot (), new ChangeBounds ());
            setupViewData ();
        }
    }
    
    /**
     * A listener can be used to get notified when we fail to remove
     * a view from its parent. This is used in {@link com.itsaky.androidide.DesignerActivity}.
     *
     * When the user tries to remove the outermost view of the inflated layout,
     * AttrEditorSheet fails to remove the view from its parent ({@link IView#getParent()} is null for root XML layout).
     * In this case, DesignerActivity check if the view that we were trying to delete is the root layout or not.
     * If it is the root layout, then it deletes the layout from the layout container.
     *
     * @author Akash Yadav
     */
    public interface OnViewDeletionFailedListener {
    
        /**
         * @param view The view that was not removed from its parent.
         * @return {@code true} if the listener handled the error. {@code false} otherwise.
         */
        boolean onDeletionFailed (IView view);
    }
}