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
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.itsaky.androidide.adapters.WidgetGroupItemAdapter;
import com.itsaky.androidide.adapters.WidgetItemAdapter;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityDesignerBinding;
import com.itsaky.androidide.fragments.sheets.AttrEditorSheet;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.models.UIWidget;
import com.itsaky.androidide.models.UIWidgetGroup;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.ui.WidgetDragData;
import com.itsaky.androidide.ui.WidgetDragListener;
import com.itsaky.androidide.ui.WidgetDragShadowBuilder;
import com.itsaky.androidide.ui.WidgetTouchListener;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IInflateListener;
import com.itsaky.inflater.ILayoutInflater;
import com.itsaky.inflater.IView;
import com.itsaky.inflater.IViewGroup;
import com.itsaky.inflater.LayoutInflaterConfiguration;
import com.itsaky.inflater.impl.BaseView;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DesignerActivity extends StudioActivity
    implements WidgetItemAdapter.OnDragStartListener,
        AttrEditorSheet.OnViewDeletionFailedListener,
        ILayoutInflater.ContextProvider {

  public static final String KEY_LAYOUT_PATH = "designer_layoutPath";
  public static final String KEY_RES_DIRS = "designer_resDirs";
  public static final String KEY_GENERATED_CODE = "designer_xmlCode";
  public static final String DRAGGING_WIDGET_TAG = "DRAGGING_WIDGET";
  public static final String DRAGGING_WIDGET_MIME = "application/ide_widget";
  private static final ILogger LOG = ILogger.newInstance("DesignerActivity");
  public final String EDITOR_SHEET_DIALOG_TAG = "attribute_editor_dialog";
  private final boolean isTablet = DeviceUtils.isTablet();
  private final List<UIWidgetGroup> widgetGroups = new ArrayList<>();
  private ActivityDesignerBinding mBinding;
  private UIWidgetGroup checkedWidgetCategory;
  private ILayoutInflater layoutInflater;
  private IViewGroup inflatedRoot;
  private File layout;
  private boolean inflationFailed = false;
  private AttrEditorSheet mEditorSheet;
  // This will make sure to apply listeners, background and data to view that are inflated from
  // XML.
  private final IInflateListener mInflateListener =
      new IInflateListener() {

        @Override
        public void onBeginInflate() {}

        @Override
        public void onInflateView(IView view, IViewGroup parent) {
          if (view instanceof IViewGroup) {
            View v = view.asView();
            v.setBackgroundResource(R.drawable.bg_design_container_background);
            v.setOnDragListener(getOnDragListener((IViewGroup) view));
          }

          setupInflatedView(view);
          setDragDataToInflatedView(view);
        }

        @Override
        public void onApplyAttribute(IAttribute attr, IView view) {}

        @Override
        public void onFinishInflate(IView rootView) {
          if (rootView instanceof IViewGroup) {
            inflatedRoot = (IViewGroup) rootView;
          }
        }
      };

  @Override
  public void onBackPressed() {

    if (inflationFailed) {
      super.onBackPressed();
      return;
    }

    // When the user presses the back button, set the activity result and finish this activity

    final var progress = new ProgressSheet();
    progress.setSubMessageEnabled(true);
    progress.setShowTitle(false);
    progress.setMessage(getString(R.string.title_generating_xml));
    progress.setSubMessage(getString(R.string.please_wait));
    progress.show(getSupportFragmentManager(), "generate_code_progress");

    final var future =
        CompletableFuture.supplyAsync(
            () -> BaseView.XML_HEADER.concat("\n").concat(inflatedRoot.generateCode()));
    future.whenComplete(
        (s, throwable) -> {
          progress.dismiss();

          if (throwable != null) {
            notifyXmlGenerationFailed(throwable);
            return;
          }

          try {
            finishWithResult(future.get());
          } catch (ExecutionException | InterruptedException e) {
            notifyXmlGenerationFailed(e);
          }
        });
  }

  private void notifyXmlGenerationFailed(Throwable error) {
    final var errorMessage = getString(R.string.msg_generate_xml_failed);
    LOG.error(errorMessage, error);

    DialogUtils.newYesNoDialog(
            this,
            getString(R.string.title_code_generation_failed),
            getString(R.string.msg_code_generation_failed),
            (dialog1, which) -> finishWithError(),
            (dialog1, which) -> dialog1.dismiss())
        .show();
  }

  private void finishWithError() {
    final var intent = new Intent();
    setResult(-123, intent);
    finish();
  }

  private void finishWithResult(String result) {
    final var intent = new Intent();
    intent.putExtra(KEY_GENERATED_CODE, result);
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  public void onDragStarted(View view) {
    mBinding.getRoot().closeDrawer(GravityCompat.START);
  }

  @Override
  public boolean onDeletionFailed(@NonNull IView view) {
    final var v = view.asView();
    if (mBinding.layoutContainer.indexOfChild(v) >= 0) {
      mBinding.layoutContainer.removeView(v);
      return true;
    }
    return false;
  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(mBinding.toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    final var toggle =
        new ActionBarDrawerToggle(
            this, mBinding.getRoot(), mBinding.toolbar, R.string.app_name, R.string.app_name) {

          @Override
          public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if (isTablet) {
              mBinding.mainContainer.setTranslationX(drawerView.getWidth() * slideOffset);
            }
          }
        };
    mBinding.getRoot().addDrawerListener(toggle);
    toggle.syncState();

    final var extras = getIntent().getExtras();
    final var path = extras.getString(KEY_LAYOUT_PATH, null);
    final var name = path.substring(path.lastIndexOf(File.separator) + 1);
    final var resDirs = extras.getStringArrayList(KEY_RES_DIRS);
    getSupportActionBar().setTitle(name);

    try {
      this.layout = new File(path);
      LOG.debug("Inflating layout file:", this.layout);
      LOG.debug("Resource directories:", TextUtils.join(",\n", resDirs));

      //noinspection deprecation
      final var pd = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
      final var dirs = resDirs.stream().map(File::new).collect(Collectors.toSet());
      final var future = getApp().createInflaterConfig(this, dirs);

      TaskExecutor.executeAsyncProvideError(
          future::get,
          (config, throwable) -> {
            pd.dismiss();

            if (config == null || throwable != null) {
              LOG.error("Unable to create inflater configuration", throwable);
              return;
            }

            inflatePath(path, config);
          });
    } catch (Throwable th) {
      onLayoutInflationFailed(th);
    }

    setupWidgets();

    // Always open the drawer if this device is a tablet.
    if (isTablet) {
      mBinding.getRoot().setScrimColor(Color.TRANSPARENT);
      mBinding.getRoot().openDrawer(GravityCompat.START);
      mBinding.getRoot().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }
  }

  @Override
  protected View bindLayout() {
    this.mBinding = ActivityDesignerBinding.inflate(getLayoutInflater());
    return mBinding.getRoot();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    LOG.logThis();
    if (isAttrEditorShowing()) {
      getAttrEditorSheet().dismiss();
    }

    this.mEditorSheet = null;

    // Release the reference to inflate listener and context from the layout inflater
    // Failing to do so will lead getSupportFragmentManager() to return a destroyed fragment
    // manager
    try {
      if (layoutInflater == null) {
        LOG.debug("Skipping layout inflater context reference release. Inflater is null");
        return;
      }

      this.layoutInflater.unregisterListener(mInflateListener);
      this.layoutInflater.resetContextProvider(null);
    } catch (Throwable e) {
      LOG.error("Unable to release activity references from ILayoutInflater", e);
    }
    mBinding = null;
  }

  private boolean isAttrEditorShowing() {
    return getAttrEditorSheet().getDialog() != null && getAttrEditorSheet().getDialog().isShowing();
  }

  private AttrEditorSheet getAttrEditorSheet() {
    return this.mEditorSheet == null
        ? mEditorSheet = new AttrEditorSheet().setLayout(this.layout)
        : mEditorSheet;
  }

  protected void onLayoutInflationFailed(Throwable th) {
    mBinding.layoutContainer.removeAllViews();
    mBinding.layoutContainer.addView(createErrorText(th));
    LOG.error(getString(R.string.err_cannot_inflate_layout), th);
    inflationFailed = true;
  }

  private void inflatePath(String path, LayoutInflaterConfiguration config) {
    try {
      this.layoutInflater = ILayoutInflater.newInstance(config);
      this.layoutInflater.resetContextProvider(this);
      this.layoutInflater.registerInflateListener(this.mInflateListener);
      final IView view = this.layoutInflater.inflatePath(path, mBinding.layoutContainer);

      if (this.inflatedRoot != null) {
        this.inflatedRoot.asView().setOnDragListener(getOnDragListener(this.inflatedRoot));
      }

      mBinding.layoutContainer.addView(view.asView());
      mBinding.layoutContainer.setOnClickListener(
          v -> mBinding.getRoot().openDrawer(GravityCompat.START));

      tryDismiss();
    } catch (Throwable error) {
      onLayoutInflationFailed(error);
    }
  }

  private void tryDismiss() {
    final var manager = getSupportFragmentManager();
    final var frag = manager.findFragmentByTag(EDITOR_SHEET_DIALOG_TAG);
    if (frag != null) {
      final var transaction = manager.beginTransaction();
      transaction.remove(frag);
      transaction.commit();
    }
  }

  private void setupInflatedView(@NonNull IView view) {
    final var listener =
        new WidgetTouchListener(view, this::onLayoutViewClick, this::onLayoutViewLongClick);
    view.asView().setOnTouchListener(listener);

    setDragDataToInflatedView(view);
  }

  private boolean onLayoutViewLongClick(@NonNull IView view) {
    final var shadow = new WidgetDragShadowBuilder(view.asView());
    final var item = new ClipData.Item(DesignerActivity.DRAGGING_WIDGET_TAG);
    final var clip =
        new ClipData(
            DesignerActivity.DRAGGING_WIDGET_TAG,
            new String[] {DesignerActivity.DRAGGING_WIDGET_MIME},
            item);
    final var data = view.getExtraData();

    view.asView().startDragAndDrop(clip, shadow, data, 0);
    view.removeFromParent();

    return true;
  }

  private void onLayoutViewClick(@NonNull IView view) {
    if (isAttrEditorShowing()) {
      getAttrEditorSheet().dismiss();
    }

    getAttrEditorSheet().setSelectedView(view);

    if (!getSupportFragmentManager().isDestroyed()) {
      getAttrEditorSheet().show(getSupportFragmentManager(), EDITOR_SHEET_DIALOG_TAG);
    }
  }

  private void setupWidgets() {
    final var android = new UIWidgetGroup(getString(R.string.widget_group_android));
    android.addChild(
        new UIWidget(getString(R.string.widget_button), R.drawable.ic_widget_button, Button.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_checkbox), R.drawable.ic_widget_checkbox, CheckBox.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_checked_textview),
            R.drawable.ic_widget_checked_textview,
            CheckedTextView.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_edittext), R.drawable.ic_widget_edittext, EditText.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_image_button),
            R.drawable.ic_widget_image,
            ImageButton.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_image_view), R.drawable.ic_widget_image, ImageView.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_progressbar),
            R.drawable.ic_widget_progress,
            ProgressBar.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_radio_button),
            R.drawable.ic_widget_radio_button,
            RadioButton.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_seekbar), R.drawable.ic_widget_seekbar, SeekBar.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_spinner), R.drawable.ic_widget_spinner, Spinner.class));
    android.addChild(
        new UIWidget(
            getString(R.string.widget_textview), R.drawable.ic_widget_textview, TextView.class));

    // IMPORTANT: Do not select any other groups here
    android.setSelected(true);
    this.checkedWidgetCategory = android;
    widgetGroups.add(android);

    final var layouts = new UIWidgetGroup(getString(R.string.widget_group_layouts));
    layouts.addChild(
        new UIWidget(
            getString(R.string.layout_linear), R.drawable.ic_widget_linear, LinearLayout.class));
    layouts.addChild(
        new UIWidget(
            getString(R.string.layout_relative),
            R.drawable.ic_widget_relative,
            RelativeLayout.class));

    widgetGroups.add(layouts);

    final var adapter = new WidgetGroupItemAdapter(widgetGroups, this::showWidgetGroup);
    mBinding.groupItems.setAdapter(adapter);

    showWidgetGroup(android);
  }

  @SuppressLint("NotifyDataSetChanged")
  private void showWidgetGroup(@NonNull UIWidgetGroup group) {
    final var children = group.getChildren();
    this.mBinding.widgetItems.setAdapter(new WidgetItemAdapter(children, this));
    this.checkedWidgetCategory.setSelected(false);
    this.checkedWidgetCategory = group;
    this.checkedWidgetCategory.setSelected(true);

    TransitionManager.beginDelayedTransition(
        mBinding.navigation,
        new TransitionSet().addTransition(new Slide(Gravity.END)).addTransition(new Fade()));

    this.mBinding.groupItems.getAdapter().notifyDataSetChanged();
  }

  @NonNull
  private TextView createErrorText(Throwable th) {
    final TextView error = new TextView(this);
    error.setText(createErrorMessage(th));
    error.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    error.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    error.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
    error.setGravity(Gravity.CENTER);
    return error;
  }

  private CharSequence createErrorMessage(final Throwable th) {
    final var sb = new StringBuilder();

    if (th == null) {
      sb.append("Unknown error");
      return sb;
    }

    var err = th;
    var first = true;
    while (err != null) {
      if (!first) {
        sb.append("\n");
      }
      sb.append(err.getMessage());
      err = err.getCause();
    }

    return sb;
  }

  @NonNull
  @Contract("_ -> new")
  private View.OnDragListener getOnDragListener(IViewGroup group) {
    // WidgetDragListener cannot be reused
    // This is because they keep a reference to the view group in which dragged view will be
    // added

    return new WidgetDragListener(this, group, this::setupInflatedView);
  }

  private void setDragDataToInflatedView(@NonNull IView view) {
    view.setExtraData(new WidgetDragData(true, view, null));
  }
}
