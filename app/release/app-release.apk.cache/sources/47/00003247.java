package com.itsaky.androidide.xml.widgets.internal.util;

import com.itsaky.androidide.xml.widgets.Widget;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Intrinsics$$ExternalSyntheticCheckNotZero0;

/* compiled from: DefaultWidget.kt */
/* loaded from: classes.dex */
public final class DefaultWidget implements Widget {
    public final String qualifiedName;
    public final String simpleName;
    public final List<String> superclasses;
    public final int type;

    /* JADX WARN: Incorrect types in method signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/util/List<Ljava/lang/String;>;)V */
    public DefaultWidget(String str, String str2, int i, List superclasses) {
        Intrinsics$$ExternalSyntheticCheckNotZero0.m(i, "type");
        Intrinsics.checkNotNullParameter(superclasses, "superclasses");
        this.simpleName = str;
        this.qualifiedName = str2;
        this.type = i;
        this.superclasses = superclasses;
    }

    @Override // com.itsaky.androidide.xml.widgets.Widget
    public final String getQualifiedName() {
        return this.qualifiedName;
    }

    @Override // com.itsaky.androidide.xml.widgets.Widget
    public final String getSimpleName() {
        return this.simpleName;
    }

    @Override // com.itsaky.androidide.xml.widgets.Widget
    public final List<String> getSuperclasses() {
        return this.superclasses;
    }

    @Override // com.itsaky.androidide.xml.widgets.Widget
    public final int getType$enumunboxing$() {
        return this.type;
    }
}