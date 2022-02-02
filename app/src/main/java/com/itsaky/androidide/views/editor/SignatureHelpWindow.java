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

package com.itsaky.androidide.views.editor;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureInformation;

import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;

/**
 * An {@link EditorPopupWindow} used to show signature help.
 * @author Akash Yadav
 */
public class SignatureHelpWindow extends EditorPopupWindow {
    
    private final TextView text;
    
    private static final Logger LOG = Logger.instance ("SignatureHelpWindow");
    
    /**
     * Create a signature help popup window for editor
     *
     * @param editor The editor.
     */
    public SignatureHelpWindow (@NonNull CodeEditor editor) {
        super (editor, getFeatureFlags ());
        
        final var context = editor.getContext ();
        final var dp8 = SizeUtils.dp2px (8);
        final var dp4 = SizeUtils.dp2px (4);
        
        this.text = new TextView (context);
        this.text.setBackground (createBackground ());
        this.text.setTextColor (ContextCompat.getColor (context, R.color.primaryTextColor));
        this.text.setTextSize (TypedValue.COMPLEX_UNIT_SP, 14);
        this.text.setClickable (false);
        this.text.setFocusable (false);
        this.text.setPaddingRelative (dp8, dp4, dp8, dp4);
        
        setContentView (this.text);
    }
    
    private Drawable createBackground () {
        GradientDrawable background = new GradientDrawable ();
        background.setShape (GradientDrawable.RECTANGLE);
        background.setColor (0xff212121);
        background.setStroke (1, 0xffffffff);
        background.setCornerRadius (8);
        return background;
    }
    
    public void setSignatureHelp (SignatureHelp signature) {
        
        if (signature == null || signature.getSignatures ().isEmpty ()) {
            if (isShowing ()) {
                dismiss ();
            }
            
            return;
        }
    
        final var signatureText = createSignatureText (signature);
        
        if (signatureText == null) {
            return;
        }
        
        this.text.setText (signatureText);
    }
    
    private CharSequence createSignatureText (SignatureHelp signature) {
        final var signatures = signature.getSignatures ();
        final var activeSignature = signature.getActiveSignature ();
        final var activeParameter = signature.getActiveParameter ();
        final SpannableStringBuilder sb = new SpannableStringBuilder ();
        
        if (activeSignature < 0 || activeParameter < 0) {
            return null;
        }
        
        if (activeSignature >= signatures.size ()) {
            return null;
        }
        
        // remove all with non-applicable signatures
        signatures.removeIf (info -> info.getParameters ().size () >= activeParameter);
        
        for (var i = 0; i<signatures.size (); i++) {
            final var info = signatures.get (i);
            sb.append (formatSignature (info, activeParameter, i == activeSignature));
            sb.append ('\n');
    
        }
        
        return sb;
    }
    
    private static int getFeatureFlags () {
        return FEATURE_SCROLL_AS_CONTENT | FEATURE_HIDE_WHEN_FAST_SCROLL;
    }
    
    /**
     * Formats (highlights) a method signature
     *  @param signature Signature information
     * @param paramIndex Currently active parameter index
     * @param isCurrentSignature <code>true</code> if the given signature is the active signature.
     */
    @NonNull
    private CharSequence formatSignature (@NonNull SignatureInformation signature, int paramIndex, boolean isCurrentSignature) {
        String name = signature.getLabel();
        name = name.substring(0, name.indexOf("("));
        
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(name, new ForegroundColorSpan (0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        var params = signature.getParameters();
        for(int i=0;i<params.size();i++) {
            int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
            final var info = params.get(i);
            if(i == params.size() - 1) {
                sb.append(info.getLabel() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(info.getLabel() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(",", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
            }
        }
        sb.append(")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        if (isCurrentSignature) {
            sb.setSpan (new BackgroundColorSpan (Color.parseColor ("#e0e0e0")), 0, sb.length (), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        return sb;
    }
    
}
