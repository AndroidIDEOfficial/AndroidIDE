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

import static com.itsaky.androidide.resources.R.drawable;
import static com.itsaky.androidide.resources.R.string;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.adapters.SimpleIconTextAdapter;
import com.itsaky.androidide.adapters.XMLAttributeListAdapter;
import com.itsaky.androidide.app.IDEApplication;
import com.itsaky.androidide.databinding.LayoutAttrEditorSheetBinding;
import com.itsaky.androidide.databinding.LayoutAttrEditorSheetItemBinding;
import com.itsaky.androidide.fragments.attr.BaseValueEditorFragment;
import com.itsaky.androidide.models.IconTextListItem;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.attrinfo.models.Attr;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IView;
import com.itsaky.inflater.impl.UiAttribute;
import com.itsaky.toaster.Toaster;
import com.itsaky.toaster.ToastUtilsKt;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AttrEditorSheet extends BottomSheetDialogFragment
    implements SimpleIconTextAdapter.OnBindListener<IconTextListItem>,
        Consumer<Attr>,
        BaseValueEditorFragment.OnValueChangeListener {

  private static final List<IconTextListItem> VIEW_ACTIONS = new ArrayList<>();
  private static final ILogger LOG = ILogger.newInstance("AttrBottomSheet");
  private AttributeListSheet mAttrListSheet;
  private AttrValueEditorSheet mValueEditorSheet;
  private IView selectedView;

  @SuppressWarnings({"FieldCanBeLocal", "unused"})
  private File layout;

  private LayoutAttrEditorSheetBinding binding;
  private OnViewDeletionFailedListener mDeletionFailedListener;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding =
        LayoutAttrEditorSheetBinding.inflate(LayoutInflater.from(getContext()), container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (VIEW_ACTIONS.isEmpty()) {
      Collections.addAll(
          VIEW_ACTIONS,
          IconTextListItem.create(getString(string.msg_viewaction_add_attr), drawable.ic_add),
          IconTextListItem.create(getString(string.title_viewaction_delete), drawable.ic_delete),
          IconTextListItem.create(
              getString(string.msg_viewaction_select_parent), drawable.ic_view_select_parent));
    }

    setupViewData();
  }

  private void setupViewData() {
    binding.actionsList.setAdapter(new SimpleIconTextAdapter(VIEW_ACTIONS).setOnBindListener(this));

    if (dismissIfSelectedNull()) {
      LOG.error("Cannot edit attributes of a null view.");
      return;
    }

    binding.widgetName.setText(this.selectedView.getXmlTag());
    binding.attrList.setAdapter(
        new XMLAttributeListAdapter(
            this.selectedView.getAttributes().stream()
                .map(XMLAttribute::new)
                .collect(Collectors.toList()),
            this::onAttrClick));
  }

  private void onAttrClick(
      LayoutAttrEditorSheetItemBinding binding, @NonNull XMLAttribute attribute) {

    if (dismissIfSelectedNull()) {
      LOG.error("Cannot edit attributes of a null view.");
      return;
    }

    final var format = attribute.findFormat();
    if (format == -1) {
      ToastUtilsKt.toast(getString(string.msg_no_attr_format), Toaster.Type.ERROR);
      LOG.error(getString(string.msg_no_attr_format), attribute);
      return;
    }

    showValueEditorSheet(attribute);
  }

  private void showValueEditorSheet(@NonNull XMLAttribute attribute) {
    getValueEditorSheet(attribute).show(getChildFragmentManager(), "attr_value_editor_sheet");
  }

  @NonNull
  public AttrValueEditorSheet getValueEditorSheet(@NonNull XMLAttribute attribute) {

    if (mValueEditorSheet == null) {
      mValueEditorSheet = AttrValueEditorSheet.newInstance(attribute);
    }

    mValueEditorSheet.setAttribute(attribute);
    return mValueEditorSheet;
  }

  private boolean dismissIfSelectedNull() {
    if (selectedView == null) {
      dismiss();
      return true;
    }
    return false;
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    mDeletionFailedListener = (OnViewDeletionFailedListener) context;
  }

  public AttrEditorSheet setLayout(File layout) {
    Objects.requireNonNull(layout);

    this.layout = layout;
    return this;
  }

  public void setSelectedView(IView view) {
    this.selectedView = view;
  }

  @Override
  public void postBind(
      IconTextListItem item, @NonNull SimpleIconTextAdapter.VH holder, int position) {
    final var binding = holder.binding;
    binding.getRoot().setOnClickListener(v -> onViewActionClick(position));
  }

  private void onViewActionClick(int position) {
    if (dismissIfSelectedNull()) {
      return;
    }

    if (position == 0) { // Add attribute
      var tag = this.selectedView.getXmlTag();
      if ("include".equals(tag) || "merge".equals(tag)) {
        tag = "View";
      }

      final var attrs = IDEApplication.getInstance().attrInfo();
      final var style = attrs.getStyle(tag);
      if (style == null) {
        LOG.error("Unable to retrieve attributes for tag:", tag);
        return;
      }

      final var attributes = new TreeSet<Attr>(Comparator.comparing(attr -> attr.name));
      attributes.addAll(style.attributes);
      attributes.addAll(attrs.NO_PARENT.attributes);

      final var widgetInfo = IDEApplication.getInstance().widgetInfo();
      final var widget = widgetInfo.getWidgetBySimpleName(tag);
      if (widget != null) {
        for (var superclass : widget.superclasses) {
          if ("java.lang.Object".equals(superclass)) {
            break;
          }

          final var simpleName = superclass.substring(superclass.lastIndexOf(".") + 1);
          final var superStyle = attrs.getStyle(simpleName);
          if (superStyle != null) {
            attributes.addAll(superStyle.attributes);
          }
        }
      }

      final var parent = this.selectedView.getParent();
      if (parent != null) {
        final var parentTag = parent.getXmlTag();
        final var parentWidget = widgetInfo.getWidgetBySimpleName(parentTag);
        if (parentWidget != null) {
          final var parentLayoutParams = attrs.getStyle(parentTag + "_Layout");
          if (parentLayoutParams != null) {
            attributes.addAll(parentLayoutParams.attributes);
          }

          final var paramSuperclasses = widgetInfo.getLayoutParamSuperClasses(parentWidget.name);
          if (paramSuperclasses != null) {
            for (var superclass : paramSuperclasses) {
              if ("java.lang.Object".equals(superclass)) {
                continue;
              }

              final var split = superclass.split("\\.");
              final var simpleClassName = split[split.length - 2];
              var paramName = split[split.length - 1];
              paramName = paramName.substring(0, paramName.length() - "Params".length());
              final var superParamEntry = attrs.getStyle(simpleClassName + "_" + paramName);
              if (superParamEntry != null) {
                attributes.addAll(superParamEntry.attributes);
              }
            }
          }
        }
      }

      final var sheet = getAttrListSheet();
      sheet.setItems(filterAppliedAttributes(attributes));
      sheet.show(getChildFragmentManager(), "attr_list_sheet");

    } else if (position == 1) { // Delete
      DialogUtils.newYesNoDialog(
              getContext(),
              (dialog, which) -> {
                var handled = selectedView.removeFromParent();
                if (!handled) {
                  handled =
                      mDeletionFailedListener != null
                          && mDeletionFailedListener.onDeletionFailed(this.selectedView);
                }
                if (!handled) {
                  ToastUtilsKt.toast(getString(string.msg_view_deletion_failed), Toaster.Type.ERROR);
                } else {
                  dismiss();
                }
              },
              (dialog, which) -> dialog.dismiss())
          .show();
    } else if (position == 2) { // Select parent
      if (this.selectedView.getParent() == null) {
        ToastUtilsKt.toast(getString(string.msg_no_view_parent), Toaster.Type.ERROR);
        return;
      }

      this.selectedView = this.selectedView.getParent();

      TransitionManager.beginDelayedTransition(binding.getRoot());
      setupViewData();
    }
  }

  @NonNull
  @Contract("_ -> new")
  private List<Attr> filterAppliedAttributes(@NonNull TreeSet<Attr> attributes) {
    attributes.removeIf(attr -> this.selectedView.hasAttribute(attr.namespace, attr.name));
    return new ArrayList<>(attributes);
  }

  @NonNull
  private AttributeListSheet getAttrListSheet() {
    if (mAttrListSheet == null) {
      mAttrListSheet = new AttributeListSheet();
      mAttrListSheet.setCancelable(true);
    }

    return mAttrListSheet;
  }

  @Override
  public void accept(Attr attr) {
    addNewAttribute(attr);
  }

  private void addNewAttribute(@NonNull Attr attr) {
    final XMLAttribute attribute = new XMLAttribute(attr.namespace, attr.name, "", false);
    attribute.setAttr(attr);
    showValueEditorSheet(attribute);
  }

  @Override
  public void onValueChanged(@NonNull IAttribute attribute, String newValue) {
    if (dismissIfSelectedNull()) {
      LOG.warn("Cannot update attribute with new value. Selected view is null.");
      return;
    }

    final var attributeName = attribute.getAttributeName();
    final var namespace = attribute.getNamespace();

    if (this.selectedView.hasAttribute(namespace, attributeName)) {
      this.selectedView.updateAttribute(namespace, attributeName, newValue);
    } else {
      final var attr = new UiAttribute(namespace, attributeName, newValue);
      this.selectedView.addAttribute(attr);
    }
  }

  /**
   * A listener can be used to get notified when we fail to remove a view from its parent. This is
   * used in {@link com.itsaky.androidide.DesignerActivity DesignerActivity}.
   *
   * <p>When the user tries to remove the outermost view of the inflated layout, AttrEditorSheet
   * fails to remove the view from its parent ({@link IView#getParent()} is null for root XML
   * layout). In this case, DesignerActivity check if the view that we were trying to delete is the
   * root layout or not. If it is the root layout, then it deletes the layout from the layout
   * container.
   *
   * @author Akash Yadav
   */
  public interface OnViewDeletionFailedListener {

    /**
     * @param view The view that was not removed from its parent.
     * @return {@code true} if the listener handled the error. {@code false} otherwise.
     */
    boolean onDeletionFailed(IView view);
  }
}
