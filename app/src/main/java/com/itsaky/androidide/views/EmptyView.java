package com.itsaky.androidide.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class EmptyView extends RelativeLayout {
    
    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        init();
    }

    private void init() {
        
        removeAllViews();
        
        TextView text = new TextView(getContext());
        text.setText(com.itsaky.androidide.R.string.msg_empty_view);
        text.setTextColor(ContextCompat.getColor(getContext(), com.itsaky.androidide.R.color.secondaryTextColor_light));
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(CENTER_IN_PARENT);
        
        addView(text, params);
        
    }
}
