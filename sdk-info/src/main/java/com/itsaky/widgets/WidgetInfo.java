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
    
    public WidgetInfo (final Context ctx) throws IOException {
        this.widgets = new TreeMap<>();
        readWidgets(ctx.getResources());
    }
    
    public Widget getWidgetBySimpleName (String simpleName) {
        return this.widgets.getOrDefault(simpleName, null);
    }
    
    public Collection<Widget> getWidgets() {
        return this.widgets.values();
    }

    private void readWidgets(final Resources resources) throws IOException {
        final InputStream in = resources.openRawResource(com.itsaky.sdkinfo.R.raw.widgets);
        if(in == null) return;
        
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
    }
    
    private String simpleName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
	}
}
