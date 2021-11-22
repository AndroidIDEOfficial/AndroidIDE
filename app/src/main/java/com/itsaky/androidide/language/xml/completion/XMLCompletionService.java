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


package com.itsaky.androidide.language.xml.completion;

import com.google.gson.JsonObject;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.attrinfo.models.Attr;
import com.itsaky.widgets.WidgetInfo;
import com.itsaky.widgets.models.Widget;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import java.io.IOException;

public class XMLCompletionService {
    
    private AttrInfo attrs;
    private WidgetInfo widgets;
	
	private boolean attrsInitialized = false;
	private boolean widgetsInitialized = false;
    
    private static final String INITIAL_TAG_ATTRIBUTES =
    "android:layout_width=\"wrap_content\"\n" + 
    "android:layout_height=\"wrap_content\"";
    
	public XMLCompletionService(final AttrInfo attrs, final WidgetInfo widgets) {
		this.attrs = attrs;
        this.widgets = widgets;
	}
	
	public boolean isInitiated() {
        return attrsInitialized && widgetsInitialized;
	}
    
	public List<CompletionItem> complete(CharSequence content, int index, int line, int column, String prefix) {
		final List<CompletionItem> result = new ArrayList<>();
        
        if (!isInitiated()) {
            return handleCompletionResults(result);
        }
        
		if(prefix.startsWith("<") || prefix.startsWith("</")) {
			boolean slash = false;
			prefix = prefix.substring(1).trim();
			if(prefix.charAt(0) == '/') {
				slash = true;
				prefix = prefix.substring(1);
			}
			for(Widget view : widgets.getWidgets()) {
				if(view.simpleName.toLowerCase(Locale.US).startsWith(prefix))
					result.add(widgetNameAsCompletion(view, slash));
			}
			return handleCompletionResults(result);
		} else {
			final IsInValueScanner scanner = new IsInValueScanner(index);
			final String name = scanner.scan(content);
			if(name != null) {
				final String attrName = name.contains(":") ? name.substring(name.indexOf(":") + 1) : name;
                if (attrs.getAttrs().containsKey(attrName)) {
                    Attr attr = attrs.getAttrs().get(attrName);
                    if(attr.hasPossibleValues()) {
                        Set<String> values = attr.possibleValues;
                        for(String value : values) 
                            if(value.toLowerCase(Locale.US).startsWith(prefix))
                                result.add(valueAsCompletion(value));
                    }
                }
			} else {
				for(Map.Entry<String, Attr> entry : attrs.getAttrs().entrySet()) {
					Attr attr = entry.getValue();
					if(attr.name.toLowerCase(Locale.US).startsWith(prefix))
						result.add(attrAsCompletion(attr));
				}
			}
		}
		return handleCompletionResults(result);
	}

	private CompletionItem valueAsCompletion(String value) {
		CompletionItem item = new CompletionItem();
		item.setLabel(value);
        item.setDetail("Attribute value");
        item.setInsertText(value);
        item.setInsertTextFormat(InsertTextFormat.PlainText);
        item.setSortText("0" + value);
        item.setKind(CompletionItemKind.Value);
		return item;
	}

	private CompletionItem attrAsCompletion(Attr attr) {
		CompletionItem item = new CompletionItem();
        item.setLabel(attr.name);
        item.setDetail("Attribute");
        item.setInsertText(createAttributeInsertText(attr));
        item.setInsertTextFormat(InsertTextFormat.PlainText);
        item.setSortText("1" + attr.name);
        item.setKind(CompletionItemKind.Snippet);
		return item;
	}

    private String createAttributeInsertText(Attr attr) {
        StringBuilder xml = new StringBuilder();
        xml.append(attr.prefix);
        xml.append(":");
        xml.append(attr.name);
        xml.append("=");
        xml.append("\"");
        
        if(attr.prefix.equals("android")
        && attr.name.equals("id")) {
            xml.append("@+id/");
        }
        
        xml.append("$0");
        xml.append("\"");
        
        return xml.toString();
    }

	private CompletionItem widgetNameAsCompletion(Widget view, boolean slash) {
		CompletionItem item = new CompletionItem();
        item.setLabel(view.simpleName);
        item.setDetail(view.name);
        item.setInsertText(createTagInsertText(view, slash));
        item.setInsertTextFormat(InsertTextFormat.PlainText);
        item.setSortText("2" + view.simpleName);
        item.setKind(CompletionItemKind.Class);
        
        // Required to show API information in completion list
        final JsonObject data = new JsonObject();
        data.addProperty("className", view.name);
        
        item.setData(data);
        
		return item;
	}

    private String createTagInsertText(Widget view, boolean closing) {
        StringBuilder sb = new StringBuilder();
        
        // Don't append leading '<' and trailing '>'
        // They may have been already inserted by editor upon typing '<'
        
        sb.append(view.simpleName);
        
        if(!closing) {
            sb.append("\n");
            sb.append(INITIAL_TAG_ATTRIBUTES);
            sb.append("\n$0/");
        } else {
            sb.append(">$0");
        }
        
        return sb.toString();
    }
	
	private List<CompletionItem> handleCompletionResults(List<CompletionItem> result) {
		return result;
	}
    
	private class IsInValueScanner {
		
		private int cursorIndex;
        
		public IsInValueScanner(int cursorIndex) {
			this.cursorIndex = cursorIndex;
        }
		
		private boolean containsCursor(Token token) {
			int start = token.getStartIndex();
			int end = token.getStopIndex();
			return start <= cursorIndex && cursorIndex <= end;
		}
		
		public String scan(CharSequence xmlContent) {
			try {
				XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(xmlContent.toString())));
				Token token;
				String attrName = null;
				while((token = lexer.nextToken()) != null && token.getType() != XMLLexer.EOF) {
					if(token.getType() == XMLLexer.STRING && containsCursor(token)) {
                        String text = token.getText();
                        if(text.startsWith("\""))
                            return attrName;
					} else if(token.getType() == XMLLexer.Name) {
						attrName = token.getText();
					}
				}
			} catch (Throwable e) {}
			return null;
		}
		
	}
}
