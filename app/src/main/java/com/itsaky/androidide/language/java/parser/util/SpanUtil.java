package com.itsaky.androidide.language.java.parser.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.language.java.parser.model.ClassDescription;

/**
 * Created by duy on 19/07/2017.
 */

public class SpanUtil {


    public static Spannable formatClass(Context context, ClassDescription item) {
        SpannableString simpleName = new SpannableString(item.getSimpleName());
        SpannableString packageName = new SpannableString("(" + item.getPackageName() + ")");
        simpleName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,
                android.R.color.primary_text_dark)), 0, simpleName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        packageName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,
                android.R.color.secondary_text_dark)), 0, packageName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableStringBuilder().append(simpleName).append(packageName);
    }
}
