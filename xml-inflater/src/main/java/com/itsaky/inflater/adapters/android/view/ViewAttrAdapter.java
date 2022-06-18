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
package com.itsaky.inflater.adapters.android.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IAttributeAdapter;
import com.itsaky.inflater.IDTable;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.util.CommonParseUtils;
import com.itsaky.xml.INamespace;

/**
 * Handles attributes common to all android views. Also, LayoutParams related issues are also
 * handled by this adapter.
 *
 * @author Akash Yadav
 */
public class ViewAttrAdapter extends CommonParseUtils implements IAttributeAdapter {

  protected static final ILogger LOG = ILogger.newInstance("BaseViewAttrAdapter");

  public ViewAttrAdapter(@NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public void setResourceFinder(IResourceTable resourceFinder) {
    this.resourceFinder = resourceFinder;
  }

  @Override
  public boolean isApplicableTo(View view) {
    return true; // Can be applied to any view
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final INamespace namespace = attribute.getNamespace();
    final String name = attribute.getAttributeName();
    final String value = attribute.getValue();
    final ViewGroup.LayoutParams params = view.getLayoutParams();
    final Context context = view.getContext();

    if (!canHandleNamespace(namespace)) {
      return false;
    }

    boolean handled = true;
    switch (name) {
      case "layout_height":
        params.height = parseDimension(value, -2, displayMetrics);
        break;
      case "layout_width":
        params.width = parseDimension(value, -2, displayMetrics);
        break;
      case "alpha":
        view.setAlpha(parseFloat(value));
        break;
      case "background":
        view.setBackground(parseDrawable(value, context));
        break;
      case "backgroundTint":
        view.setBackgroundTintList(parseColorStateList(value, context));
        break;
      case "backgroundTintMode":
        view.setBackgroundTintMode(parsePorterDuffMode(value));
        break;
      case "clipToOutline":
        view.setClipToOutline(parseBoolean(value));
        break;
      case "contentDescription":
        view.setContentDescription(parseString(value));
        break;
      case "contextClickable":
        view.setContextClickable(parseBoolean(value));
        break;
      case "defaultFocusHighlightEnabled":
        view.setDefaultFocusHighlightEnabled(parseBoolean(value));
        break;
      case "drawingCacheQuality":
        view.setDrawingCacheQuality(parseDrawingCacheQuality(value));
        break;
      case "duplicateParentState":
        view.setDuplicateParentStateEnabled(parseBoolean(value));
        break;
      case "elevation":
        view.setElevation(parseDimension(value, 0, displayMetrics));
        break;
      case "fadeScrollbars":
        view.setScrollbarFadingEnabled(parseBoolean(value));
        break;
      case "fadingEdgeLength":
        view.setFadingEdgeLength(parseDimension(value, 0, displayMetrics));
        break;
      case "filterTouchesWhenObscured":
        view.setFilterTouchesWhenObscured(parseBoolean(value));
        break;
      case "foreground":
        view.setForeground(parseDrawable(value, context));
        break;
      case "foregroundGravity":
        view.setForegroundGravity(parseGravity(value));
        break;
      case "foregroundTint":
        view.setForegroundTintList(parseColorStateList(value, context));
        break;
      case "foregroundTintMode":
        view.setForegroundTintMode(parsePorterDuffMode(value));
        break;
      case "id":
        view.setId(parseId(value));
        break;
      case "minHeight":
        view.setMinimumHeight(parseDimension(value, 0, displayMetrics));
        break;
      case "minWidth":
        view.setMinimumWidth(parseDimension(value, 0, displayMetrics));
        break;
      case "padding":
        final int padding = parseDimension(value, 0, displayMetrics);
        view.setPaddingRelative(padding, padding, padding, padding);
        break;
      case "paddingLeft":
        final int paddingLeft = parseDimension(value, 0, displayMetrics);
        view.setPadding(
            paddingLeft, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        break;
      case "paddingTop":
        final int paddingTop = parseDimension(value, 0, displayMetrics);
        view.setPadding(
            view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());
        break;
      case "paddingRight":
        final int paddingRight = parseDimension(value, 0, displayMetrics);
        view.setPadding(
            view.getPaddingLeft(), view.getPaddingTop(), paddingRight, view.getPaddingBottom());
        break;
      case "paddingBottom":
        final int paddingBottom = parseDimension(value, 0, displayMetrics);
        view.setPadding(
            view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), paddingBottom);
        break;
      case "paddingStart":
        final int paddingStart = parseDimension(value, 0, displayMetrics);
        view.setPaddingRelative(
            paddingStart, view.getPaddingTop(), view.getPaddingEnd(), view.getPaddingBottom());
        break;
      case "paddingEnd":
        final int paddingEnd = parseDimension(value, 0, displayMetrics);
        view.setPaddingRelative(
            view.getPaddingStart(), view.getPaddingTop(), paddingEnd, view.getPaddingBottom());
        break;
      case "rotation":
        view.setRotation(parseFloat(value));
        break;
      case "rotationX":
        view.setRotationX(parseFloat(value));
        break;
      case "rotationY":
        view.setRotationY(parseFloat(value));
        break;
      case "scaleX":
        view.setScaleX(parseFloat(value));
        break;
      case "scaleY":
        view.setScaleY(parseFloat(value));
        break;
      case "scrollX":
        view.setScrollX((int) parseFloat(value));
        break;
      case "scrollY":
        view.setScrollY((int) parseFloat(value));
        break;
      case "textAlignment":
        view.setTextAlignment(parseTextAlignment(value));
        break;
      case "textDirection":
        view.setTextDirection(parseTextDirection(value));
        break;
      case "tooltipText":
        view.setTooltipText(parseString(value));
        break;
      case "transformPivotX":
        view.setPivotX(parseFloat(value));
        break;
      case "transformPivotY":
        view.setPivotY(parseFloat(value));
        break;
      case "translationX":
        view.setTranslationX(parseFloat(value));
        break;
      case "translationY":
        view.setTranslationY(parseFloat(value));
        break;
      case "translationZ":
        view.setTranslationZ(parseFloat(value));
        break;
      case "visibility":
        view.setVisibility(parseVisibility(value));
        break;
      default:
        handled = false;
        break;
    }

    // ----- Handle attributes related to parent ------

    if (!handled && params instanceof LinearLayout.LayoutParams) {
      handled =
          handleLinearLayoutParams((LinearLayout.LayoutParams) params, name, value, displayMetrics);
    }

    if (!handled && params instanceof RelativeLayout.LayoutParams) {
      handled = handleRelativeLayoutParams((RelativeLayout.LayoutParams) params, name, value);
    }

    if (!handled && params instanceof FrameLayout.LayoutParams) {
      handled = handleFrameLayoutParams((FrameLayout.LayoutParams) params, name, value);
    }

    if (!handled && params instanceof ViewGroup.MarginLayoutParams) {
      handled =
          handleMarginParams((ViewGroup.MarginLayoutParams) params, name, value, displayMetrics);
    }

    view.setLayoutParams(params);

    return handled;
  }

  protected int parseId(@NonNull String value) {
    if (value.startsWith("@id/")) {
      final String name = value.substring("@id/".length());
      return IDTable.getId(name);
    } else if (value.startsWith("@+id/")) {
      final String name = value.substring("@+id/".length());
      return IDTable.newId(name);
    }
    return View.NO_ID;
  }

  protected boolean handleRelativeLayoutParams(
      RelativeLayout.LayoutParams params, @NonNull String name, String value) {
    boolean handled = true;
    switch (name) {
      case "layout_above":
        params.addRule(RelativeLayout.ABOVE, parseId(value));
        break;
      case "layout_alignBaseline":
        params.addRule(RelativeLayout.ALIGN_BASELINE, parseId(value));
        break;
      case "layout_alignBottom":
        params.addRule(RelativeLayout.ALIGN_BOTTOM, parseId(value));
        break;
      case "layout_alignEnd":
        params.addRule(RelativeLayout.ALIGN_END, parseId(value));
        break;
      case "layout_alignLeft":
        params.addRule(RelativeLayout.ALIGN_LEFT, parseId(value));
        break;
      case "layout_alignParentTop":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_TOP, params);
        break;
      case "layout_alignParentBottom":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_BOTTOM, params);
        break;
      case "layout_alignParentStart":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_START, params);
        break;
      case "layout_alignParentEnd":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_END, params);
        break;
      case "layout_alignParentLeft":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_LEFT, params);
        break;
      case "layout_alignParentRight":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.ALIGN_PARENT_RIGHT, params);
        break;
      case "layout_alignRight":
        params.addRule(RelativeLayout.ALIGN_RIGHT, parseId(value));
        break;
      case "layout_alignStart":
        params.addRule(RelativeLayout.ALIGN_START, parseId(value));
        break;
      case "layout_alignTop":
        params.addRule(RelativeLayout.ALIGN_TOP, parseId(value));
        break;
      case "layout_alignWithParentIfMissing":
        params.alignWithParent = parseBoolean(value);
        break;
      case "layout_below":
        params.addRule(RelativeLayout.BELOW, parseId(value));
        break;
      case "layout_centerHorizontal":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.CENTER_HORIZONTAL, params);
        break;
      case "layout_centerInParent":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.CENTER_IN_PARENT, params);
        break;
      case "layout_centerVertical":
        setRelativeRuleIfTrue(parseBoolean(value), RelativeLayout.CENTER_VERTICAL, params);
        break;
      case "layout_toEndOf":
        params.addRule(RelativeLayout.END_OF, parseId(value));
        break;
      case "layout_toStartOf":
        params.addRule(RelativeLayout.START_OF, parseId(value));
        break;
      case "layout_toLeftOf":
        params.addRule(RelativeLayout.LEFT_OF, parseId(value));
        break;
      case "layout_toRightOf":
        params.addRule(RelativeLayout.RIGHT_OF, parseId(value));
        break;
      default:
        handled = false;
    }
    return handled;
  }

  private void setRelativeRuleIfTrue(
      boolean condition, int rule, RelativeLayout.LayoutParams params) {
    if (condition) {
      params.addRule(rule);
    } else {
      params.removeRule(rule);
    }
  }

  protected boolean handleLinearLayoutParams(
      LinearLayout.LayoutParams params, @NonNull String name, String value, DisplayMetrics dm) {
    boolean handled = true;
    switch (name) {
      case "layout_gravity":
        params.gravity = parseGravity(value);
        break;
      case "layout_weight":
        params.weight = parseDimension(value, 1, dm);
        break;
      default:
        handled = false;
        break;
    }
    return handled;
  }

  protected boolean handleFrameLayoutParams(
      FrameLayout.LayoutParams params, @NonNull String name, String value) {
    boolean handled = true;

    if ("layout_gravity".equals(name)) {
      params.gravity = parseGravity(value);
    } else {
      handled = false;
    }

    return handled;
  }

  protected boolean handleMarginParams(
      ViewGroup.MarginLayoutParams params, @NonNull String name, String value, DisplayMetrics dm) {
    boolean handled = true;
    switch (name) {
      case "layout_margin":
        final int margin = parseDimension(value, 0, dm);
        params.setMargins(margin, margin, margin, margin);
        break;
      case "layout_marginLeft":
        params.leftMargin = parseDimension(value, 0, dm);
        break;
      case "layout_marginTop":
        params.topMargin = parseDimension(value, 0, dm);
        break;
      case "layout_marginRight":
        params.rightMargin = parseDimension(value, 0, dm);
        break;
      case "layout_marginBottom":
        params.bottomMargin = parseDimension(value, 0, dm);
        break;
      case "layout_marginStart":
        params.setMarginStart(parseDimension(value, 0, dm));
        break;
      case "layout_marginEnd":
        params.setMarginEnd(parseDimension(value, 0, dm));
        break;
      default:
        handled = false;
        break;
    }

    return handled;
  }

  protected boolean canHandleNamespace(@NonNull IAttribute attr) {
    return canHandleNamespace(attr.getNamespace());
  }

  protected boolean canHandleNamespace(INamespace namespace) {
    return INamespace.ANDROID.equals(namespace);
  }

  protected int parseDrawingCacheQuality(@NonNull String value) {
    switch (value) {
      case "high":
        return View.DRAWING_CACHE_QUALITY_HIGH;
      case "low":
        return View.DRAWING_CACHE_QUALITY_LOW;
      case "auto":
      default:
        return View.DRAWING_CACHE_QUALITY_AUTO;
    }
  }

  protected int parseVisibility(@NonNull String value) {
    switch (value) {
      case "gone":
        return View.GONE;
      case "invisible":
        return View.INVISIBLE;
      case "visible":
      default:
        return View.VISIBLE;
    }
  }

  protected int parseTextAlignment(@NonNull String value) {
    switch (value) {
      case "center":
        return View.TEXT_ALIGNMENT_CENTER;
      case "gravity":
        return View.TEXT_ALIGNMENT_GRAVITY;
      case "textEnd":
        return View.TEXT_ALIGNMENT_TEXT_END;
      case "textStart":
        return View.TEXT_ALIGNMENT_TEXT_START;
      case "viewEnd":
        return View.TEXT_ALIGNMENT_VIEW_END;
      case "viewStart":
        return View.TEXT_ALIGNMENT_VIEW_START;
      case "inherit":
      default:
        return View.TEXT_ALIGNMENT_INHERIT;
    }
  }

  protected int parseTextDirection(@NonNull String value) {
    switch (value) {
      case "anyRtl":
        return View.TEXT_DIRECTION_ANY_RTL;
      case "firstStrong":
        return View.TEXT_DIRECTION_FIRST_STRONG;
      case "firstStrongLtr":
        return View.TEXT_DIRECTION_FIRST_STRONG_LTR;
      case "firstStrongRtl":
        return View.TEXT_DIRECTION_FIRST_STRONG_RTL;
      case "locale":
        return View.TEXT_DIRECTION_LOCALE;
      case "ltr":
        return View.TEXT_DIRECTION_LTR;
      case "rtl":
        return View.TEXT_DIRECTION_RTL;
      case "inherit":
      default:
        return View.TEXT_DIRECTION_INHERIT;
    }
  }

  protected PorterDuff.Mode parsePorterDuffMode(@NonNull String mode) {
    switch (mode) {
      case "add":
        return PorterDuff.Mode.ADD;
      case "multiply":
        return PorterDuff.Mode.MULTIPLY;
      case "screen":
        return PorterDuff.Mode.SCREEN;
      case "src_atop":
        return PorterDuff.Mode.SRC_ATOP;
      case "src_in":
        return PorterDuff.Mode.SRC_IN;
      case "src_over":
        return PorterDuff.Mode.SRC_OVER;
      default:
        return PorterDuff.Mode.SRC;
    }
  }

  protected float parseFloat(String value) {
    try {
      return Float.parseFloat(value);
    } catch (Throwable th) {
      return 1f;
    }
  }

  protected float parseFloat(String value, float def) {
    try {
      return Float.parseFloat(value);
    } catch (Throwable th) {
      return def;
    }
  }

  protected long parseLong(String value, long def) {
    try {
      return Long.parseLong(value);
    } catch (Throwable th) {
      return def;
    }
  }

  protected int parseDimension(final String value, int defaultValue, final DisplayMetrics dm) {

    if (value == null) {
      return defaultValue;
    }

    char c = value.charAt(0);
    if (Character.isDigit(c)) {
      // A dimension value which starts with a digit. E.g.: 1dp, 12sp, 123px, etc.
      StringBuilder dimensionVal = new StringBuilder();
      int index = 0;
      while (index < value.length() && Character.isDigit(c = value.charAt(index))) {
        dimensionVal.append(c);
        index++;
      }

      final int dimen = Integer.parseInt(dimensionVal.toString());
      final String dimensionType = value.substring(index);
      return (int) TypedValue.applyDimension(getUnitForDimensionType(dimensionType), dimen, dm);
    } else if (c == '@') {
      String name = value.substring("@dimen/".length());
      return parseDimension(resourceFinder.findDimension(name), defaultValue, dm);
    } else if (Character.isLetter(c)) {
      // This could be one of the following :
      // 1. match_parent
      // 2. wrap_content
      // 3. fill_parent
      switch (value) {
        case "match_parent":
        case "fill_parent":
          return ViewGroup.LayoutParams.MATCH_PARENT;
        case "wrap_content":
        default:
          return ViewGroup.LayoutParams.WRAP_CONTENT;
      }
    }

    return defaultValue;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
  protected boolean isApi28() {
    return Build.VERSION.SDK_INT >= 28;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
  protected boolean isApi29() {
    return Build.VERSION.SDK_INT >= 29;
  }

  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
  protected boolean isApi30() {
    return Build.VERSION.SDK_INT >= 30;
  }
}
