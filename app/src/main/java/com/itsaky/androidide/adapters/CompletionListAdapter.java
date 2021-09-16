package com.itsaky.androidide.adapters;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutCompletionItemBinding;
import com.itsaky.androidide.models.CompletionItemWrapper;
import com.itsaky.androidide.models.CompletionListItem;
import com.itsaky.androidide.models.SuggestItem;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.utils.Either;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.apiinfo.models.ClassInfo;
import com.itsaky.apiinfo.models.MethodInfo;
import com.itsaky.lsp.CompletionItemKind;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.widget.EditorCompletionAdapter;
import java.util.Locale;
import com.google.gson.JsonArray;
import com.itsaky.apiinfo.models.FieldInfo;
import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itsaky.apiinfo.models.Info;
import android.text.TextUtils;

public class CompletionListAdapter extends EditorCompletionAdapter {
    
    private static final Logger LOG = Logger.instance("CompletionListAdapter");
    
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
		if (desc == null || desc.isEmpty())
			binding.completionDetail.setVisibility(View.GONE);

        if (isCurrentCursorPosition)
            binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.completionList_backgroundSelected));
        else binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.completionList_background));

        binding.completionApiInfo.setVisibility(View.GONE);
        
        if (e != null && e.isLeft() && e.getLeft() instanceof CompletionItemWrapper) {
            showApiInfoIfNeeded((CompletionItemWrapper) e.getLeft(), binding.completionApiInfo);
        }
        
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            
            ViewGroup.LayoutParams p = binding.completionIconText.getLayoutParams();
            p.height = binding.getRoot().getHeight();
            binding.completionIconText.setLayoutParams(p);
        });

        return binding.getRoot();
    }

    private void showApiInfoIfNeeded(final CompletionItemWrapper item, final TextView completionApiInfo) {
        
        new Thread(() -> {
            final ApiInfo info = StudioApp.getInstance().getApiInfo();
            boolean hasRead = info != null && info.hasRead();
            boolean isValid = isValidForApiVersion(item.getItem());
            
            if (hasRead && isValid) {

                JsonElement element = item.getItem().data;
                if (element == null || !element.isJsonObject()) return;
                JsonObject data = element.getAsJsonObject();
                if (!data.has("className")) return;
                
                final String className = data.get("className").getAsString();
                int kind = item.getItem().kind;
                
                ClassInfo clazz = info.getClassByName(className);
                if(clazz == null) return;
                
                Info apiInfo = clazz;

                /**
                 * If this Info is not a class info, find the right member
                 */
                if (kind == CompletionItemKind.Method
                    && data.has("erasedParameterTypes")
                    && data.has("memberName")) {
                    JsonElement erasedParameterTypesElement = data.get("erasedParameterTypes");
                    if(erasedParameterTypesElement.isJsonArray()) {
                        String simpleName = data.get("memberName").getAsString();
                        JsonArray erasedParameterTypes = erasedParameterTypesElement.getAsJsonArray();
                        String[] paramTypes = new String[erasedParameterTypes.size()];
                        for(int i=0;i<erasedParameterTypes.size();i++) {
                            paramTypes[i] = erasedParameterTypes.get(i).getAsString();
                        }
                        
                        MethodInfo method = clazz.getMethod(simpleName, paramTypes);
                        
                        if(method != null) {
                            apiInfo = method;
                        }
                    }
                } else if(kind == CompletionItemKind.Field
                          && data.has("memberName")) {
                    String simpleName = data.get("memberName").getAsString();
                    FieldInfo field = clazz.getFieldByName(simpleName);
                    
                    if(field != null) {
                        apiInfo = field;
                    }
                }

                final StringBuilder infoBuilder = new StringBuilder();
                if (apiInfo != null && apiInfo.since > 1) {
                    infoBuilder.append(completionApiInfo.getContext().getString(R.string.msg_api_info_since, apiInfo.since));
                }

                if (apiInfo != null && apiInfo.removed > 0) {
                    infoBuilder.append(completionApiInfo.getContext().getString(R.string.msg_api_info_removed, apiInfo.removed));
                }

                if (apiInfo != null && apiInfo.deprecated > 0) {
                    infoBuilder.append(completionApiInfo.getContext().getString(R.string.msg_api_info_deprecated, apiInfo.deprecated));
                }
                
                ThreadUtils.runOnUiThread(() -> {
                    if(infoBuilder.toString().trim().length() > 0) {
                        completionApiInfo.setText(infoBuilder.toString().trim());
                        completionApiInfo.setVisibility(View.VISIBLE);
                    } else completionApiInfo.setVisibility(View.GONE);
                });
            }
        }).start();
    }

    private boolean isValidForApiVersion(com.itsaky.lsp.CompletionItem item) {
        if (item == null) return false;
        final int type = item.kind;
        JsonElement element = item.data;
        if (
        
        /**
         * These represent a class type
         */
        (type == CompletionItemKind.Class
            || type == CompletionItemKind.Interface
            || type == CompletionItemKind.Enum
            
        /**
         * These represent a method type
         */
            || type == CompletionItemKind.Method
            || type == CompletionItemKind.Constructor
            
        /**
         * A field type
         */
            || type == CompletionItemKind.Field)
            
            && element != null && element.isJsonObject()) {
            JsonObject data = element.getAsJsonObject();
            return data.has("className");
        }
        return false;
    }
}
