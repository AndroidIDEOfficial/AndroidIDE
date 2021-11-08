package com.itsaky.androidide.ui.view.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.itsaky.androidide.ui.inflater.IResourceFinder;
import com.itsaky.androidide.ui.view.IAttribute;

public class LinearLayoutAttrAdapter extends BaseViewGroupAttrAdapter {

    @Override
    public boolean isApplicableTo(View view) {
        return view instanceof LinearLayout;
    }

    @Override
    public boolean apply(IAttribute attribute, View view, IResourceFinder resFinder) {
        if (super.apply(attribute, view, resFinder)) {
            return true;
        }
        
        final LinearLayout linear = (LinearLayout) view;
        final String namespace = attribute.getNamespace();
        final String name = attribute.getNamespace();
        final String value = attribute.getValue();
        
        if (!canHandleNamespace(namespace)) {
            return false;
        }
        
        boolean handled = true;
        
        switch (name) {
            case "baselineAligned" :
                linear.setBaselineAligned(parseBoolean(value, resFinder));
                break;
            case "baselineAlignedChildIndex" :
                linear.setBaselineAlignedChildIndex(parseInteger(value, linear.getChildCount()));
                break;
            case "gravity" :
                linear.setGravity(parseGravity(value));
                break;
            case "measureWithLargestChild" :
                linear.setMeasureWithLargestChildEnabled(parseBoolean(value, resFinder));
                break;
            case "orientation" :
                linear.setOrientation(parseOrientation(value));
                break;
            case "weightSum" :
                linear.setWeightSum(parseFloat(value));
                break;
            default :
                handled = false;
                break;
        }
        
        return handled;
    }

    protected int parseOrientation(String value) {
        switch (value) {
            case "vertical" :
                return LinearLayout.VERTICAL;
            case "horizontal" :
            default :
                return LinearLayout.HORIZONTAL;
        }
    }
}
