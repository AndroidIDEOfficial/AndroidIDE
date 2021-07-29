package com.itsaky.androidide.models;

import android.graphics.drawable.Drawable;
import io.github.rosemoe.editor.struct.CompletionItem;
import java.util.ArrayList;
import java.util.List;

public class CompletionListItem extends CompletionItem {
    private String detail;
    private String itemType;
    private Type type;

	private List<CompletionEdit> additionalEdits;

    public CompletionListItem() {
		super("", "");
	}

	public CompletionListItem setLabel(String label) {
		this.label = label;
		return this;
	}

	public CompletionListItem setCommit(String commit) {
		this.commit = commit;
		cursorOffset(commit.length());
		return this;
	}

	public CompletionListItem setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public CompletionListItem setIcon(Drawable icon) {
		this.icon = icon;
		return this;
	}

	public String getLabel() {
		return this.label;
	}

	public String getCommit() {
		return this.commit;
	}

	public String getDesc() {
		return this.desc;
	}

	public Drawable getIcon() {
		return this.icon;
	}

    public CompletionListItem setType(Type type) {
        this.type = type;
		return this;
    }

    public Type getType() {
        return type;
    }

    public CompletionListItem setItemType(String type) {
        this.itemType = type;
		return this;
    }

    public String getItemType() {
        return itemType;
    }

    public CompletionListItem setDetail(String detail) {
        this.detail = detail;
		return this;
    }

    public String getDetail() {
        return detail;
    }

	public CompletionListItem setAdditionalEdits(List<CompletionEdit> edits) {
		this.additionalEdits = edits;
		return this;
	}

	public List<CompletionEdit> getAdditionalEdits() {
		if (this.additionalEdits == null)
			this.additionalEdits = new ArrayList<>();

		return additionalEdits;
	}

	public CompletionListItem addAdditionalEdit(CompletionEdit edit) {
		if (edit != null) {
			getAdditionalEdits().add(edit);
		}
		return this;
	}
	
	public boolean hasAdditionalEdits() {
		return getAdditionalEdits() != null && getAdditionalEdits().size() > 0;
	}
	
	public String getSortText() {
		if (getType() == Type.LOCAL_VARIABLE)
			return "0" + getLabel();

		if (getType() == Type.FIELD)
			return "1" + getLabel();

		if (getType() == Type.KEYWORD)
			return "2" + getLabel();

		if (getType() == Type.METHOD)
			return "3" + getLabel();

		if (getType() == Type.ENUM)
			return "4" + getLabel();

		if (getType() == Type.INTERFACE)
			return "5" + getLabel();

		if (getType() == Type.CLASS)
			return "6" + getLabel();
			
		if (getType() == Type.NOT_IMPORTED_CLASS)
			return "7" + getLabel();
			
		if (getType() == Type.ATTRIBUTE)
			return "8" + getLabel();
			
		if (getType() == Type.WIDGET)
			return "9" + getLabel();

		if (getType() == Type.OBJECT)
			return "10" + getLabel();
		return getLabel();
	}

    public static enum Type {
		ATTRIBUTE('A'),
        CLASS('C'),
		ENUM('E'),
		FIELD('F'),
		INTERFACE('I'),
		KEYWORD('K'),
		LOCAL_VARIABLE('V'),
		METHOD('M'),
		NOT_IMPORTED_CLASS(' '),
		OBJECT('O'),
		WIDGET('W');

        private char c;
        private Type(char c) {
            this.c = c;
        }

        public String getAsString() {
            return String.valueOf(c);
        }

        public char get() {
            return c;
        }
    }

	public int compareTo(CompletionListItem obj) {
		return getSortText().compareTo(obj.getSortText());
	}

    public CompletionListItem clone() {
        CompletionListItem item = new CompletionListItem();
        item.setCommit(getCommit());
        item.setDesc(getDesc());
        item.setDetail(getDetail());
        item.setIcon(getIcon());
        item.setItemType(getItemType());
        item.setLabel(getLabel());
        item.setType(getType());
        return item;
    }

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof CompletionListItem) {
			CompletionListItem that = (CompletionListItem) obj;
			return this.getLabel().equals(that.getLabel())
				&& this.getDesc().equals(that.getDesc())
				&& this.getDetail().equals(that.getDetail())
				&& this.getCommit().equals(that.getCommit())
				&& this.getItemType() == that.getItemType()
				&& this.hasAdditionalEdits() == that.hasAdditionalEdits()
				&& this.getAdditionalEdits().size() == that.getAdditionalEdits().size()
				&& this.getAdditionalEdits() == that.getAdditionalEdits();
		}
		return false;
	}

    @Override
    public String toString() {
        return
			"["
			+ "\ncommit   = " + getCommit()
			+ "\ndesc     = " + getDesc()
			+ "\ndetail   = " + getDetail()
			+ "\nicon     = " + getIcon()
			+ "\nitemType = " + getItemType()
			+ "\nlabel    = " + getLabel()
			+ "\ntype     = " + getType()
			+ "\n]";
    }
}
