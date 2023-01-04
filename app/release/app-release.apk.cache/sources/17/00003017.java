package com.itsaky.androidide.preferences.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import com.itsaky.androidide.R;
import kotlin.io.CloseableKt;

/* loaded from: classes.dex */
public final class LayoutDialogTextInputBinding {
    @NonNull
    public final TextInputLayout name;
    @NonNull
    public final LinearLayout rootView;

    public LayoutDialogTextInputBinding(@NonNull LinearLayout linearLayout, @NonNull TextInputLayout textInputLayout) {
        this.rootView = linearLayout;
        this.name = textInputLayout;
    }

    @NonNull
    public static LayoutDialogTextInputBinding inflate(@NonNull LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.layout_dialog_text_input, (ViewGroup) null, false);
        TextInputLayout textInputLayout = (TextInputLayout) CloseableKt.findChildViewById(inflate, R.id.name);
        if (textInputLayout != null) {
            return new LayoutDialogTextInputBinding((LinearLayout) inflate, textInputLayout);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(inflate.getResources().getResourceName(R.id.name)));
    }
}