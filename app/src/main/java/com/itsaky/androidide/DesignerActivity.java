/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
**************************************************************************************/


package com.itsaky.androidide;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityDesignerBinding;
import com.itsaky.androidide.ui.inflater.ILayoutInflater;
import com.itsaky.androidide.ui.view.IView;
import com.itsaky.androidide.utils.Logger;
import java.lang.reflect.Field;

public class DesignerActivity extends StudioActivity {
    
    private ActivityDesignerBinding mBinding;
    
    public static final String KEY_LAYOUT_PATH = "designer_layoutPath";
    private static final Logger LOG = Logger.instance("DesignerActivity");
    
    @Override
    protected View bindLayout() {
        mBinding = ActivityDesignerBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        
        final Bundle extras = getIntent().getExtras();
        final String path = extras.getString(KEY_LAYOUT_PATH, null);
        
        try {
            final ILayoutInflater inflater = getApp().getLayoutInflater();
            inflater.resetContextProvider(newContextProvider());
            
            final IView view = inflater.inflatePath(path, mBinding.realContainer);
            mBinding.realContainer.addView(view.asView());
        } catch (Throwable th) {
            mBinding.realContainer.removeAllViews();
            mBinding.realContainer.addView(createErrorText(th));
        }
    }

    private ILayoutInflater.ContextProvider newContextProvider() {
        return new ILayoutInflater.ContextProvider() {
            @Override
            public Context getContext() {
                return DesignerActivity.this;
            }
        };
    }
    
    private TextView createErrorText (Throwable th) {
        final TextView error = new TextView (this);
        error.setText(ThrowableUtils.getFullStackTrace(th));
        error.setLayoutParams(new ViewGroup.LayoutParams (-2, -2));
        error.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        error.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        return error;
    }
    
    private String printRClassHierarchy () throws IllegalArgumentException, IllegalAccessException {
        return printClassHierarchy(android.R.class, 0);
    }
    
    private String printClassHierarchy (Class<?> clazz, int indent) throws IllegalArgumentException, IllegalAccessException {
        final StringBuilder sb = new StringBuilder();
        sb.append("class " + clazz.getName() + " {");
        newLineIndent(sb, ++indent);
        
        sb.append("fields : [");
        newLineIndent(sb, ++indent);
        printFields (sb, clazz, indent);
        newLineIndent(sb, --indent);
        sb.append("]");
        
        newLineIndent(sb, indent);
        sb.append("classes : [");
        newLineIndent(sb, ++indent);
        printClasses (sb, clazz, indent);
        newLineIndent(sb, --indent);
        sb.append("]");
        
        return sb.toString();
    }

    private void newLineIndent(StringBuilder sb, int indent) {
        sb.append("\n");
        sb.append(repeat(" ", indent * 4));
    }

    private void printFields(StringBuilder sb, Class<?> clazz, int indent) throws IllegalArgumentException, IllegalAccessException {
        boolean first = true;
        for (Field f : clazz.getDeclaredFields()) {
            if (!first) {
                newLineIndent(sb, indent);
            }
            if(!f.isAccessible()) {
                f.setAccessible(true);
            }
            sb.append(f.getName());
            sb.append(" = ");
            if (f.getType() == int[].class) {
                printIntArr(sb, (int[]) f.get(null));
            } else if (f.getType() == int.class) {
                sb.append(f.get(null));
            }
            first = false;
        }
    }
    
    private void printClasses(StringBuilder sb, Class<?> clazz, int indent) throws IllegalArgumentException, IllegalAccessException {
        boolean first = true;
        for (Class<?> c : clazz.getClasses()) {
            if (!first) {
                newLineIndent(sb, indent);
            }
            sb.append("class " + c.getSimpleName() + " [");
            newLineIndent(sb, ++indent);
            printFields(sb, c, indent);
            newLineIndent(sb, --indent);
            sb.append("]");
            first = false;
        }
    }
    
    private String repeat (String to, int till) {
        String result = "";
        for (int i=1;i<=till;i++) {
            result += to;
        }
        
        return result;
    }
    
    private void printIntArr (StringBuilder sb, int[] arr) {
        boolean first = true;
        sb.append("[");
        for (int i : arr) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(i);
            first = false;
        }
        sb.append("]");
        
    }
}
