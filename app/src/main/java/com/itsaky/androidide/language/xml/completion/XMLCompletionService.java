package com.itsaky.androidide.language.xml.completion;

import com.itsaky.androidide.language.xml.lexer.XMLLexer;
import com.itsaky.androidide.models.CompletionListItem;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.itsaky.androidide.utils.Logger;

public class XMLCompletionService {
	private HashMap<String, Attr> attrs;
	private List<String> widgets;
	private final File ANDROID_ATTRS;
	private final File ANDROID_WIDGETS;
	
	private boolean initiated = false;
	
	public XMLCompletionService() {
		this.ANDROID_ATTRS = new File(Environment.BOOTCLASSPATH.getParentFile(), "data/res/values/attrs.xml");
		this.ANDROID_WIDGETS = new File(Environment.BOOTCLASSPATH.getParentFile(), "data/widgets.txt");
		this.attrs = new HashMap<>();
		this.widgets = new ArrayList<>();
		if(ANDROID_ATTRS.exists() && ANDROID_ATTRS.isFile()) {
			addAttributesFrom(ANDROID_ATTRS, true);
		}

		if(ANDROID_WIDGETS.exists()) {
			processWidgetNames();
		}
		initiated = true;
	}
	
	public boolean isInitiated() {
		return initiated;
	}
	
	
	public List<CompletionListItem> complete(CodeEditor editor, String prefix) {
		prefix = prefix.toLowerCase(Locale.US).trim();
		final int line = editor.getCursor().getLeftLine();
		final int column = editor.getCursor().getLeftColumn();
		final List<CompletionListItem> result = new ArrayList<>();
		if(prefix.startsWith("<") || prefix.startsWith("</")) {
			boolean slash = false;
			prefix = prefix.substring(1).trim();
			if(prefix.charAt(0) == '/') {
				slash = true;
				prefix = prefix.substring(1);
			}
			for(String name : widgets) {
				if(simpleName(name).toLowerCase(Locale.US).contains(prefix))
					result.add(widgetNameAsCompletion(name, slash));
			}
			return handleResults(result);
		} else {
			final IsInValueScanner scanner = new IsInValueScanner(editor.getCursor().getLeft());
			final String name = scanner.scan(editor.getText().toString());
			if(name != null) {
				final String attrName = name.contains(":") ? name.substring(name.indexOf(":") + 1) : name;
                if (attrs.containsKey(attrName)) {
                    Attr attr = attrs.get(attrName);
                    if(attr.hasPossibleValues()) {
                        Set<String> values = attr.possibleValues;
                        for(String value : values) 
                            if(value.toLowerCase(Locale.US).contains(prefix))
                                result.add(valueAsCompletion(value));
                    }
                }
			} else {
				for(Map.Entry<String, Attr> entry : attrs.entrySet()) {
					Attr attr = entry.getValue();
					if(attr.name.toLowerCase(Locale.US).contains(prefix))
						result.add(attrAsCompletion(attr));
				}
			}
		}
		return handleResults(result);
	}

	private CompletionListItem valueAsCompletion(String value) {
		CompletionListItem item = new CompletionListItem();
		item.setLabel(value)
			.setCommit(value)
			.setDesc("Attribute Value")
			.setDetail(item.getDesc())
			.setItemType("int")
			.setType(CompletionListItem.Type.ATTRIBUTE)
			.cursorOffset(item.getCommit().length());
		return item;
	}

	private CompletionListItem attrAsCompletion(Attr attr) {
		CompletionListItem item = new CompletionListItem();
		item.setLabel(attr.name)
			.setCommit(attr.prefix + ":" + attr.name + "=\"" + (attr.prefix.equals("android") && attr.name.equals("id") ? "@+id/\"" : "\""))
			.setDesc("Attribute")
			.setDetail(item.getDesc())
			.setItemType("int")
			.setType(CompletionListItem.Type.ATTRIBUTE)
			.cursorOffset(item.getCommit().length() - 1);
		return item;
	}

	private CompletionListItem widgetNameAsCompletion(String name, boolean slash) {
		CompletionListItem item = new CompletionListItem();
		final String n = simpleName(name);
		item.setLabel(n)
			.setCommit("<" + (slash ? "/" : "") + n)
			.setDesc(name)
			.setDetail(item.getDesc())
			.setItemType("  ")
			.setType(CompletionListItem.Type.WIDGET);
		return item;
	}
	
	private String simpleName(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}
	
	private List<CompletionListItem> handleResults(List<CompletionListItem> result) {
		return result;
	}
	
	private void processWidgetNames() {
		String read = FileUtil.readFile(ANDROID_WIDGETS.getAbsolutePath());
		if(read != null && read.length() > 0) {
			widgets.clear();
			for(String line : read.split("\\n")) {
				String name = line.split("\\s")[0];
				name = name.substring(1);
				widgets.add(name);
			}
			Collections.sort(widgets);
		}
	}

	private void addAttributesFrom(File f, boolean isAndroid) {
		try {
			Document doc = Jsoup.parse(f, null);
			Elements attrs = doc.getElementsByTag("resources").first().getElementsByTag("attr");
			for(Element attr : attrs) {
				Attr a = new Attr(attr.attr("name"), isAndroid);
				if(a.name.contains(":")) {
					String[] split = a.name.split(":");
					a.prefix = split[0];
					a.name = split[1];
				}
				if(attr.hasAttr("format") && attr.attr("format").contains("boolean")) {
					a.possibleValues.add("true");
					a.possibleValues.add("false");
				}
				Elements enums = attr.getElementsByTag("enum");
				Elements flags = attr.getElementsByTag("flag");
				if(enums != null && enums.size() > 0) {
					for(Element e : enums)
						a.possibleValues.add(e.attr("name"));
				}
				if(flags != null && flags.size() > 0) {
					for(Element e : flags)
						a.possibleValues.add(e.attr("name"));
				}
				if(this.attrs.containsKey(a.name)) {
					Attr b = this.attrs.get(a.name);
					if(b != null && b.hasPossibleValues())
						a.possibleValues.addAll(b.possibleValues);
				}
				this.attrs.put(a.name, a);
			}
		} catch (Throwable th) {}
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
		
		public String scan(String xmlContent) {
			try {
				XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(xmlContent)));
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
	
	private class Attr {
		public String prefix;
		public String name;
		
		public Set<String> possibleValues;

		public Attr(String name, boolean isAndroid) {
			this.name = name;
			this.prefix = isAndroid ? "android" : "app";
			this.possibleValues = new HashSet<>();
		}
		
		public boolean hasPossibleValues() {
			return possibleValues != null && possibleValues.size() > 0;
		}

		@Override
		public String toString() {
			return String.format("[%s=%s]", name, possibleValues.toString());
		}
	}
}
