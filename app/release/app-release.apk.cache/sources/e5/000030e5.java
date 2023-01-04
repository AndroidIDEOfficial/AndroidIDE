package com.itsaky.androidide.treesitter;

import com.google.protobuf.ByteString$BoundedByteString$$ExternalSyntheticOutline0;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class TSNode {
    private int context0;
    private int context1;
    private int context2;
    private int context3;
    private long id;
    private TSTree mTree;
    private long tree;

    private TSNode() {
    }

    private native TSNode getChildAt(int i);

    private native TSNode getChildByFieldName(byte[] bArr, int i);

    private native TSNode getNamedChildAt(int i);

    public List<TSNode> findChildrenWithType(String str, boolean z, boolean z2) {
        int childCount;
        TSNode child;
        if (z) {
            return findChildrenWithTypeReverse(str, z2);
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            if (z2) {
                childCount = getNamedChildCount();
            } else {
                childCount = getChildCount();
            }
            if (i < childCount) {
                if (z2) {
                    child = getNamedChild(i);
                } else {
                    child = getChild(i);
                }
                if (!child.isNull() && Objects.equals(str, child.getType())) {
                    arrayList.add(child);
                }
                i++;
            } else {
                return arrayList;
            }
        }
    }

    public List<TSNode> findChildrenWithTypeReverse(String str, boolean z) {
        int childCount;
        TSNode child;
        ArrayList arrayList = new ArrayList();
        if (z) {
            childCount = getNamedChildCount();
        } else {
            childCount = getChildCount();
        }
        for (int i = childCount - 1; i > 0; i--) {
            if (z) {
                child = getNamedChild(i);
            } else {
                child = getChild(i);
            }
            if (!child.isNull() && Objects.equals(str, child.getType())) {
                arrayList.add(child);
            }
        }
        return arrayList;
    }

    public TSNode findNodeWithType(String str, boolean z) {
        int childCount;
        TSNode child;
        int i = 0;
        while (true) {
            if (z) {
                childCount = getNamedChildCount();
            } else {
                childCount = getChildCount();
            }
            if (i < childCount) {
                if (z) {
                    child = getNamedChild(i);
                } else {
                    child = getChild(i);
                }
                if (!child.isNull() && str.equals(child.getType())) {
                    return child;
                }
                i++;
            } else {
                return null;
            }
        }
    }

    public TSNode getChild(int i) {
        int childCount = getChildCount();
        if (i >= 0 && i < childCount) {
            return getChildAt(i);
        }
        throw new IndexOutOfBoundsException(ByteString$BoundedByteString$$ExternalSyntheticOutline0.m("count=", childCount, ", index=", i));
    }

    public native TSNode getChildByFieldId(int i);

    public TSNode getChildByFieldName(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return getChildByFieldName(bytes, bytes.length);
    }

    public native int getChildCount();

    public native TSNode getDescendantForByteRange(int i, int i2);

    public native TSNode getDescendantForPointRange(TSPoint tSPoint, TSPoint tSPoint2);

    public native int getEndByte();

    public native TSPoint getEndPoint();

    public native String getFieldNameForChild(int i);

    public native TSNode getFirstChildForByte(int i);

    public native TSNode getFirstNamedChildForByte(int i);

    public TSNode getNamedChild(int i) {
        int namedChildCount = getNamedChildCount();
        if (i >= 0 && i < namedChildCount) {
            return getNamedChildAt(i);
        }
        throw new IndexOutOfBoundsException(ByteString$BoundedByteString$$ExternalSyntheticOutline0.m("count=", namedChildCount, ", index=", i));
    }

    public native int getNamedChildCount();

    public native TSNode getNamedDescendantForByteRange(int i, int i2);

    public native TSNode getNamedDescendantForPointRange(TSPoint tSPoint, TSPoint tSPoint2);

    public native TSNode getNextNamedSibling();

    public native TSNode getNextSibling();

    public native String getNodeString();

    public native TSNode getParent();

    public native TSNode getPreviousNamedSibling();

    public native TSNode getPreviousSibling();

    public native int getStartByte();

    public native TSPoint getStartPoint();

    public native int getSymbol();

    public TSTree getTree() {
        if (this.mTree == null) {
            this.mTree = new TSTree(this.tree);
        }
        return this.mTree;
    }

    public native String getType();

    public native boolean hasChanges();

    public native boolean hasErrors();

    public native boolean isEqualTo(TSNode tSNode);

    public native boolean isExtra();

    public native boolean isMissing();

    public native boolean isNamed();

    public native boolean isNull();

    public String toString() {
        String type;
        StringBuilder sb = new StringBuilder("TSNode{id=");
        sb.append(this.id);
        sb.append(", type=");
        if (isNull()) {
            type = "<null>";
        } else {
            type = getType();
        }
        sb.append(type);
        sb.append('}');
        return sb.toString();
    }

    public TSTreeCursor walk() {
        return new TSTreeCursor(this);
    }
}