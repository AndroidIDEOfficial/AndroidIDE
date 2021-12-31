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
package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.adapters.WidgetGroupItemAdapter;
import com.itsaky.androidide.adapters.WidgetItemAdapter;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.ActivityDesignerBinding;
import com.itsaky.androidide.fragments.sheets.AttrEditorSheet;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.models.UIWidget;
import com.itsaky.androidide.models.UIWidgetGroup;
import com.itsaky.androidide.ui.WidgetDragData;
import com.itsaky.androidide.ui.WidgetDragListener;
import com.itsaky.androidide.ui.WidgetDragShadowBuilder;
import com.itsaky.androidide.ui.WidgetTouchListener;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IInflateListener;
import com.itsaky.layoutinflater.ILayoutInflater;
import com.itsaky.layoutinflater.IView;
import com.itsaky.layoutinflater.IViewGroup;
import com.itsaky.toaster.Toaster;

import org.eclipse.lsp4j.jsonrpc.CompletableFutures;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DesignerActivity extends StudioActivity implements WidgetItemAdapter.OnDragStartListener {
    
    private ActivityDesignerBinding mBinding;
    private UIWidgetGroup checkedWidgetCategory;
    private IViewGroup inflatedRoot;
    
    private AttrEditorSheet mEditorSheet;
    
    public static final String KEY_LAYOUT_PATH = "designer_layoutPath";
    public static final String KEY_GENERATED_CODE = "designer_xmlCode";
    public static final String DRAGGING_WIDGET_TAG = "DRAGGING_WIDGET";
    public static final String DRAGGING_WIDGET_MIME = "application/ide_widget";
    
    private static final Logger LOG = Logger.instance ("DesignerActivity");
    
    private final boolean isTablet = DeviceUtils.isTablet ();
    private final List<UIWidgetGroup> widgetGroups = new ArrayList<> ();
    
    // This will make sure to apply listeners, background and data to view that are inflated from XML.
    private final IInflateListener mInflateListener = new IInflateListener () {
        
        @Override
        public void onBeginInflate () {
        }
        
        @Override
        public void onInflateView (IView view, IViewGroup parent) {
            if (view instanceof IViewGroup) {
                View v = view.asView ();
                v.setBackgroundResource (R.drawable.bg_design_container_background);
                v.setOnDragListener (getOnDragListener ((IViewGroup) view));
            }
            
            setupInflatedView (view);
            setDragDataToInflatedView (view);
        }
        
        @Override
        public void onApplyAttribute (IAttribute attr, IView view) {
        
        }
        
        @Override
        public void onFinishInflate (IView rootView) {
            if (rootView instanceof IViewGroup) {
                inflatedRoot = (IViewGroup) rootView;
            }
        }
    };
    
    @Override
    protected View bindLayout () {
        this.mBinding = ActivityDesignerBinding.inflate (getLayoutInflater ());
        return mBinding.getRoot ();
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        setSupportActionBar (mBinding.toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        
        final var toggle = new ActionBarDrawerToggle (this, mBinding.getRoot (), mBinding.toolbar, R.string.app_name, R.string.app_name) {
            
            @Override
            public void onDrawerSlide (View drawerView, float slideOffset) {
                super.onDrawerSlide (drawerView, slideOffset);
                if (isTablet) {
                    mBinding.mainContainer.setTranslationX (drawerView.getWidth () * slideOffset);
                }
            }
        };
        mBinding.getRoot ().addDrawerListener (toggle);
        toggle.syncState ();
        
        final var extras = getIntent ().getExtras ();
        final var path = extras.getString (KEY_LAYOUT_PATH, null);
        final var name = path.substring (path.lastIndexOf (File.separator) + 1);
        getSupportActionBar ().setTitle (name);
        
        try {
            final ILayoutInflater inflater = getApp ().getLayoutInflater ();
            inflater.resetContextProvider (newContextProvider ());
            inflater.registerInflateListener (this.mInflateListener);
            final IView view = inflater.inflatePath (path, mBinding.layoutContainer);
            
            if (this.inflatedRoot != null) {
                this.inflatedRoot.asView ().setOnDragListener (getOnDragListener (this.inflatedRoot));
            }
            
            mBinding.layoutContainer.addView (view.asView ());
            mBinding.layoutContainer.setOnClickListener (v -> mBinding.getRoot ().openDrawer (GravityCompat.START));
        } catch (Throwable th) {
            mBinding.layoutContainer.removeAllViews ();
            mBinding.layoutContainer.addView (createErrorText (th));
            LOG.error (getString (R.string.err_cannot_inflate_layout), th);
        }
        
        setupWidgets ();
        
        // Always open the drawer if this device is a tablet.
        if (isTablet) {
            mBinding.getRoot ().setScrimColor (Color.TRANSPARENT);
            mBinding.getRoot ().openDrawer (GravityCompat.START);
            mBinding.getRoot ().setDrawerLockMode (DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }
    
    @Override
    public void onBackPressed () {
        // When the user presses the back button, set the activity result and finish this activity
        
        final var progress = new ProgressSheet ();
        progress.setSubMessageEnabled (true);
        progress.setShowTitle (false);
        progress.setMessage (getString (R.string.title_generating_xml));
        progress.setSubMessage (getString (R.string.please_wait));
        progress.show (getSupportFragmentManager (), "generate_code_progress");
        
        final var future = CompletableFutures.computeAsync (cancelChecker -> inflatedRoot.generateCode ());
        future.whenComplete ((s, throwable) -> {
            
            progress.dismiss ();
            
            if (future.isCompletedExceptionally () && throwable != null) {
                notifyXmlGenerationFailed (throwable);
                return;
            }
            
            try {
                finishWithResult (future.get ());
            } catch (ExecutionException | InterruptedException e) {
                notifyXmlGenerationFailed (e);
            }
        });
    }
    
    private void notifyXmlGenerationFailed (Throwable error) {
        final var errorMessage = getString (R.string.msg_generate_xml_failed);
        LOG.error (errorMessage, error);
        
        final var dialog = DialogUtils.newYesNoDialog (this,
                getString(R.string.title_code_generation_failed),
                getString(R.string.msg_code_generation_failed),
                (dialog1, which) -> finishWithError (),
                (dialog1, which) -> dialog1.dismiss ())
                
                .show ();
    }
    
    // FIXME Generated XML does not leave a space between
    //    codes of two adjacent views (in same parent)
    private void finishWithResult (String result) {
        final var intent = new Intent ();
        intent.putExtra (KEY_GENERATED_CODE, result);
        setResult (RESULT_OK, intent);
        finish ();
    }
    
    private void finishWithError () {
        final var intent = new Intent ();
        setResult (-123, intent);
        finish ();
    }
    
    @Override
    protected void onDestroy () {
        
        if (isAttrEditorShowing ()) {
            getAttrEditorSheet ().dismiss ();
        }
        
        this.mEditorSheet = null;
        
        // Release the reference to inflate listener and context from the layout inflater
        // Failing to do so will lead getSupportFragmentManager() to return a destroyed fragment manager
        try {
            final var layoutInflater = getApp ().getLayoutInflater ();
            layoutInflater.unregisterListener (mInflateListener);
            layoutInflater.resetContextProvider (null);
        } catch (Throwable e) {
            e.printStackTrace ();
        }
        
        super.onDestroy ();
    }
    
    private void setupInflatedView (@NonNull IView view) {
        final var listener = new WidgetTouchListener (
                view,
                this::onLayoutViewClick,
                this::onLayoutViewLongClick
        );
        view.asView ().setOnTouchListener (listener);
        
        setDragDataToInflatedView (view);
    }
    
    private boolean onLayoutViewLongClick (@NonNull IView view) {
        final var shadow = new WidgetDragShadowBuilder (view.asView ());
        final var item = new ClipData.Item (DesignerActivity.DRAGGING_WIDGET_TAG);
        final var clip = new ClipData (DesignerActivity.DRAGGING_WIDGET_TAG, new String[]{DesignerActivity.DRAGGING_WIDGET_MIME}, item);
        final var data = view.getExtraData ();
        
        view.asView ().startDragAndDrop (clip, shadow, data, 0);
        view.removeFromParent ();
        
        return true;
    }
    
    private void onLayoutViewClick (@NonNull IView view) {
        if (isAttrEditorShowing ()) {
            getAttrEditorSheet ().dismiss ();
        }
        
        getAttrEditorSheet ().setSelectedView (view);
        
        if (!getSupportFragmentManager ().isDestroyed ()) {
            getAttrEditorSheet ().show (getSupportFragmentManager (), "attribute_editor_dialog");
        }
    }
    
    private boolean isAttrEditorShowing () {
        return getAttrEditorSheet ().getDialog () != null && getAttrEditorSheet ().getDialog ().isShowing ();
    }
    
    private void setupWidgets () {
        final var android = new UIWidgetGroup (getString (R.string.widget_group_android));
        android.addChild (new UIWidget (getString (R.string.widget_button), R.drawable.ic_widget_button, Button.class));
        android.addChild (new UIWidget (getString (R.string.widget_checkbox), R.drawable.ic_widget_checkbox, CheckBox.class));
        android.addChild (new UIWidget (getString (R.string.widget_checked_textview), R.drawable.ic_widget_checked_textview, CheckedTextView.class));
        android.addChild (new UIWidget (getString (R.string.widget_edittext), R.drawable.ic_widget_edittext, EditText.class));
        android.addChild (new UIWidget (getString (R.string.widget_image_button), R.drawable.ic_widget_image, ImageButton.class));
        android.addChild (new UIWidget (getString (R.string.widget_image_view), R.drawable.ic_widget_image, ImageView.class));
        android.addChild (new UIWidget (getString (R.string.widget_progressbar), R.drawable.ic_widget_progress, ProgressBar.class));
        android.addChild (new UIWidget (getString (R.string.widget_radio_button), R.drawable.ic_widget_radio_button, RadioButton.class));
        android.addChild (new UIWidget (getString (R.string.widget_seekbar), R.drawable.ic_widget_seekbar, SeekBar.class));
        android.addChild (new UIWidget (getString (R.string.widget_spinner), R.drawable.ic_widget_spinner, Spinner.class));
        android.addChild (new UIWidget (getString (R.string.widget_textview), R.drawable.ic_widget_textview, TextView.class));
        
        // IMPORTANT: Do not select any other groups here
        android.setSelected (true);
        this.checkedWidgetCategory = android;
        widgetGroups.add (android);
        
        final var layouts = new UIWidgetGroup (getString (R.string.widget_group_layouts));
        layouts.addChild (new UIWidget (getString (R.string.layout_linear), R.drawable.ic_widget_linear, LinearLayout.class));
        layouts.addChild (new UIWidget (getString (R.string.layout_relative), R.drawable.ic_widget_relative, RelativeLayout.class));
        
        widgetGroups.add (layouts);
        
        final var adapter = new WidgetGroupItemAdapter (widgetGroups, this::showWidgetGroup);
        mBinding.groupItems.setAdapter (adapter);
        
        showWidgetGroup (android);
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private void showWidgetGroup (@NonNull UIWidgetGroup group) {
        final var children = group.getChildren ();
        this.mBinding.widgetItems.setAdapter (new WidgetItemAdapter (children, this));
        this.checkedWidgetCategory.setSelected (false);
        this.checkedWidgetCategory = group;
        this.checkedWidgetCategory.setSelected (true);
        
        TransitionManager.beginDelayedTransition (mBinding.navigation,
                new TransitionSet ()
                        .addTransition (new Slide (Gravity.END))
                        .addTransition (new Fade ())
        );
        
        //noinspection ConstantConditions
        this.mBinding.groupItems.getAdapter ().notifyDataSetChanged ();
    }
    
    @NonNull
    @Contract(pure = true)
    private ILayoutInflater.ContextProvider newContextProvider () {
        return () -> DesignerActivity.this;
    }
    
    @NonNull
    private TextView createErrorText (Throwable th) {
        final TextView error = new TextView (this);
        error.setText (ThrowableUtils.getFullStackTrace (th));
        error.setLayoutParams (new ViewGroup.LayoutParams (-2, -2));
        error.setTextColor (ContextCompat.getColor (this, android.R.color.black));
        error.setBackgroundColor (ContextCompat.getColor (this, android.R.color.white));
        return error;
    }
    
    @Override
    public void onDragStarted (View view) {
        mBinding.getRoot ().closeDrawer (GravityCompat.START);
    }
    
    @NonNull
    @Contract("_ -> new")
    private View.OnDragListener getOnDragListener (IViewGroup group) {
        // WidgetDragListener cannot be reused
        // This is because they keep a reference to the view group in which dragged view will be added
        
        return new WidgetDragListener (
                this,
                group,
                this::setupInflatedView);
    }
    
    private void setDragDataToInflatedView (@NonNull IView view) {
        view.setExtraData (new WidgetDragData (true, view, null));
    }
    
    private AttrEditorSheet getAttrEditorSheet () {
        return this.mEditorSheet == null
                ? mEditorSheet = new AttrEditorSheet ()
                .setDeletionFailedListener (this::onViewDeletionFailed)
                : mEditorSheet;
    }
    
    private boolean onViewDeletionFailed (@NonNull IView view) {
        final var v = view.asView ();
        if (mBinding.layoutContainer.indexOfChild (v) >= 0) {
            mBinding.layoutContainer.removeView (v);
            return true;
        }
        return false;
    }
}
