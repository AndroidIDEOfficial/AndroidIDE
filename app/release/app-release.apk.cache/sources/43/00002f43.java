package com.itsaky.androidide.preferences;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.R$id;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference0Impl;
import kotlin.reflect.KMutableProperty0;
import org.eclipse.jgit.merge.MergeMessageFormatter;

/* compiled from: editor.kt */
/* loaded from: classes.dex */
public final class CompletionsMatchLower extends SwitchPreference {
    public static final Parcelable.Creator<CompletionsMatchLower> CREATOR = new Creator();
    public final Integer icon;
    public final String key;
    public final Integer summary;
    public final int title;

    /* compiled from: editor.kt */
    /* renamed from: com.itsaky.androidide.preferences.CompletionsMatchLower$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
        public AnonymousClass1(AnonymousClass2 anonymousClass2) {
            super(1, anonymousClass2, KMutableProperty0.class, "set", "set(Ljava/lang/Object;)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Unit invoke(Boolean bool) {
            ((KMutableProperty0) this.receiver).set(Boolean.valueOf(bool.booleanValue()));
            return Unit.INSTANCE;
        }
    }

    /* compiled from: editor.kt */
    /* renamed from: com.itsaky.androidide.preferences.CompletionsMatchLower$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass2 extends MutablePropertyReference0Impl {
        public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

        public AnonymousClass2() {
            super(MergeMessageFormatter.class, "completionsMatchLower", "getCompletionsMatchLower()Z");
        }

        public final Object get$1() {
            return AutoSave$2$$ExternalSyntheticOutline0.m("idepref_editor_completions_matchLower", false);
        }

        @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KMutableProperty0
        public final void set(Object obj) {
            R$id.getPrefManager().putBoolean("idepref_editor_completions_matchLower", ((Boolean) obj).booleanValue());
        }
    }

    /* compiled from: editor.kt */
    /* renamed from: com.itsaky.androidide.preferences.CompletionsMatchLower$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass3 extends FunctionReferenceImpl implements Function0<Boolean> {
        public AnonymousClass3(AnonymousClass4 anonymousClass4) {
            super(0, anonymousClass4, KMutableProperty0.class, "get", "get()Ljava/lang/Object;", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Boolean invoke() {
            return (Boolean) ((KMutableProperty0) this.receiver).get$1();
        }
    }

    /* compiled from: editor.kt */
    /* renamed from: com.itsaky.androidide.preferences.CompletionsMatchLower$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass4 extends MutablePropertyReference0Impl {
        public static final AnonymousClass4 INSTANCE = new AnonymousClass4();

        public AnonymousClass4() {
            super(MergeMessageFormatter.class, "completionsMatchLower", "getCompletionsMatchLower()Z");
        }

        public final Object get$1() {
            return AutoSave$2$$ExternalSyntheticOutline0.m("idepref_editor_completions_matchLower", false);
        }

        @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KMutableProperty0
        public final void set(Object obj) {
            R$id.getPrefManager().putBoolean("idepref_editor_completions_matchLower", ((Boolean) obj).booleanValue());
        }
    }

    /* compiled from: editor.kt */
    /* loaded from: classes.dex */
    public static final class Creator implements Parcelable.Creator<CompletionsMatchLower> {
        @Override // android.os.Parcelable.Creator
        public final CompletionsMatchLower createFromParcel(Parcel parcel) {
            Integer valueOf;
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            String readString = parcel.readString();
            int readInt = parcel.readInt();
            Integer num = null;
            if (parcel.readInt() == 0) {
                valueOf = null;
            } else {
                valueOf = Integer.valueOf(parcel.readInt());
            }
            if (parcel.readInt() != 0) {
                num = Integer.valueOf(parcel.readInt());
            }
            return new CompletionsMatchLower(readString, readInt, valueOf, num);
        }

        @Override // android.os.Parcelable.Creator
        public final CompletionsMatchLower[] newArray(int i) {
            return new CompletionsMatchLower[i];
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CompletionsMatchLower(String key, int i, Integer num, Integer num2) {
        super(new AnonymousClass3(AnonymousClass4.INSTANCE), new AnonymousClass1(AnonymousClass2.INSTANCE));
        Intrinsics.checkNotNullParameter(key, "key");
        this.key = key;
        this.title = i;
        this.summary = num;
        this.icon = num2;
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    @Override // com.itsaky.androidide.preferences.IPreference
    public final Integer getIcon() {
        return this.icon;
    }

    @Override // com.itsaky.androidide.preferences.IPreference
    public final String getKey() {
        return this.key;
    }

    @Override // com.itsaky.androidide.preferences.IPreference
    public final Integer getSummary() {
        return this.summary;
    }

    @Override // com.itsaky.androidide.preferences.IPreference
    public final int getTitle() {
        return this.title;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel out, int i) {
        Intrinsics.checkNotNullParameter(out, "out");
        out.writeString(this.key);
        out.writeInt(this.title);
        int i2 = 0;
        Integer num = this.summary;
        if (num == null) {
            out.writeInt(0);
        } else {
            AutoSave$$ExternalSyntheticOutline0.m(out, 1, num);
        }
        Integer num2 = this.icon;
        if (num2 != null) {
            out.writeInt(1);
            i2 = num2.intValue();
        }
        out.writeInt(i2);
    }
}