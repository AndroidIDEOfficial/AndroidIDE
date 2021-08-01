package com.itsaky.androidide.adapters;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutCompletionItemBinding;
import com.itsaky.androidide.models.CompletionListItem;
import com.itsaky.androidide.models.SuggestItem;
import com.itsaky.androidide.utils.Either;
import com.itsaky.androidide.utils.TypefaceUtils;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.widget.EditorCompletionAdapter;
import java.util.Locale;

public class CompletionListAdapter extends EditorCompletionAdapter {
    @Override
    public int getItemHeight() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
    }

    @Override
    protected View getView(int position, View convertView, ViewGroup parent, boolean isCurrentCursorPosition) {
        LayoutCompletionItemBinding binding = LayoutCompletionItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);

		Either<SuggestItem, CompletionItem> e = getItem(position);
		String label = "", desc = "", type = "";
		String header = "";
		if (e != null && e.isLeft()) {
			SuggestItem s = e.getLeft();
			label = s.getName();
			desc = s.getDescription();
			type = s.getReturnType();
			header = String.valueOf(s.getTypeHeader()).toUpperCase(Locale.US);
		} else if (e != null && e.isRight()) {
			CompletionListItem item = (CompletionListItem) e.getRight();
			header = item.getType() == null ? CompletionListItem.Type.OBJECT.getAsString() :  item.getType().getAsString();
			label = item.getLabel();
			type = item.getItemType().contains(".") ? item.getItemType().substring(item.getItemType().lastIndexOf(".") + 1) : item.getItemType();
			desc = item.getType() != CompletionListItem.Type.NOT_IMPORTED_CLASS ? item.getDetail() : getContext().getString(R.string.label_not_imported, item.getDetail());
		}
		
        binding.completionIconText.setText(header);
        binding.completionLabel.setText(label);
        binding.completionType.setText(type);
        binding.completionDetail.setText(desc);
        binding.completionIconText.setTypeface(TypefaceUtils.jetbrainsMono(), Typeface.BOLD);
		if(desc == null || desc.isEmpty())
			binding.completionDetail.setVisibility(View.GONE);

        if (isCurrentCursorPosition)
            binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.completionList_backgroundSelected));
        else binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.completionList_background));

        return binding.getRoot();
    }
}
