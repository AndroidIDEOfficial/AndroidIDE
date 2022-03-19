/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.attrinfo.models.Attr;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.impl.UiAttribute;
import com.itsaky.xml.INamespace;
import java.util.Objects;
import org.jetbrains.annotations.Contract;

/**
 * A model that is used to show attributes list of a view.
 *
 * @author Akash Yadav
 */
public class XMLAttribute extends UiAttribute implements Parcelable {

    public static final Creator<XMLAttribute> CREATOR =
            new Creator<>() {
                @NonNull
                @Contract("_ -> new")
                @Override
                public XMLAttribute createFromParcel(Parcel in) {
                    return new XMLAttribute(in);
                }

                @NonNull
                @Contract(value = "_ -> new", pure = true)
                @Override
                public XMLAttribute[] newArray(int size) {
                    return new XMLAttribute[size];
                }
            };
    public Attr attr;
    /** Is this attribute applied to any view? */
    private boolean isApplied;
    /**
     * Format of this attribute. May be stored here so we don't have to look in {@link
     * com.itsaky.attrinfo.AttrInfo AttrInfo} again and again.
     */
    private int format = -1;

    public XMLAttribute(IAttribute attribute) {
        this(attribute, true);
    }

    public XMLAttribute(@NonNull IAttribute attribute, boolean isApplied) {
        this(
                attribute.getNamespace(),
                attribute.getAttributeName(),
                attribute.getValue(),
                isApplied);
    }

    public XMLAttribute(INamespace namespace, String name, String value, boolean isApplied) {
        super(namespace, name, value);
        this.isApplied = isApplied;
    }

    private XMLAttribute(@NonNull Parcel in) {
        this(
                in.readParcelable(INamespace.class.getClassLoader()),
                in.readString(),
                in.readString(),
                in.readByte() != 0);
        this.attr = in.readParcelable(Attr.class.getClassLoader());
    }

    /**
     * Create a new attribute instance with the android namespace. {@link #isApplied()} will return
     * true by default.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @return A new instance of this class with the provided data.
     */
    @NonNull
    @Contract("_, _ -> new")
    public static XMLAttribute newAndroidAttribute(String name, String value) {
        return new XMLAttribute(INamespace.ANDROID, name, value, true);
    }

    /**
     * Create a new attribute instance with the android namespace. The {@link #isApplied()} will
     * return {@code false} by default.
     *
     * @param name The name of the attribute.
     * @return The new attribute instance.
     */
    @NonNull
    @Contract("_ -> new")
    public static XMLAttribute newAndroidAttribute(String name) {
        return new XMLAttribute(INamespace.ANDROID, name, "", false);
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int findFormat() {

        if (getFormat() > 0) {
            return getFormat();
        }

        final var attrs = StudioApp.getInstance().attrInfo();
        this.attr = attrs.getAttribute(getAttributeName());
        if (this.attr != null) {
            this.format = this.attr.format;
        }

        return getFormat();
    }

    public boolean hasFormat(int format) {
        return (this.format & format) != 0;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        Objects.requireNonNull(attr);
        this.attr = attr;
        setFormat(attr.format);
    }

    @Override
    public void apply(String value) {
        super.apply(value);
        isApplied = true;
    }

    public boolean isApplied() {
        return isApplied;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(getNamespace(), flags);
        dest.writeString(getAttributeName());
        dest.writeString(getValue());
        dest.writeByte((byte) (isApplied ? 1 : 0));
        dest.writeParcelable(getAttr(), flags);
    }
}
