package com.itsaky.androidide.utils;

import android.graphics.Color;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

public class TransformUtils {

    public static MaterialContainerTransform createContainerTransformFor(View start, View end, View drawingView) {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setStartView(start);
        transform.setEndView(end);
        transform.addTarget(end);
        transform.setDrawingViewId(drawingView.getId());
        transform.setAllContainerColors(ContextCompat.getColor(drawingView.getContext(), com.itsaky.androidide.R.color.primaryDarkColor));
        transform.setElevationShadowEnabled(true);
        transform.setPathMotion(new MaterialArcMotion());
        transform.setScrimColor(Color.TRANSPARENT);
        return transform;
	}
    
}
