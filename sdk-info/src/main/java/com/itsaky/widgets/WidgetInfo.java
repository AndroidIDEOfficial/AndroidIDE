package com.itsaky.widgets;

import android.content.Context;
import android.content.res.Resources;
import com.itsaky.widgets.models.Widget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class WidgetInfo {
    
    private final SortedMap<String, Widget> widgets;
    
    public WidgetInfo (final Context ctx, final Runnable onFinish) {
        this.widgets = new TreeMap<>();
        
        readWidgets(ctx.getResources(), onFinish);
    }
    
    public Widget getWidgetBySimpleName (String simpleName) {
        return this.widgets.getOrDefault(simpleName, null);
    }
    
    public Collection<Widget> getWidgets() {
        return this.widgets.values();
    }

    private void readWidgets(final Resources resources, final Runnable onFinish) {
        final InputStream in = resources.openRawResource(com.itsaky.sdkinfo.R.raw.widgets);
        if(in == null) return;
        
        // It's good not to block the UI Thread.
        new Thread(() -> {
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while((line = reader.readLine()) != null) {
                    final String name = line.split("\\s")[0];
                    if(name == null || name.trim().isEmpty()) continue;

                    final char code = name.charAt(0);

                    final String viewName = name.substring(1);
                    final String simpleViewName = simpleName(viewName);
                    final boolean isViewGroup = code == 'L'; // L -> Layout, W -> Widget, P -> LayoutParam

                    // Don't add layout params
                    if(code != 'P') {
                        widgets.put(simpleViewName, new Widget(viewName, simpleViewName, isViewGroup));
                    }
                }
                
                if(onFinish != null) {
                    onFinish.run();
                }
            } catch (IOException e) {}
        }).start();
    }
    
    private String simpleName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
	}
}
