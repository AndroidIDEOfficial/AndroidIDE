-keepattributes Signature
-keepattributes *Annotation*
-renamesourcefileattribute FuckYou
-obfuscationdictionary dictionary.txt
-packageobfuscationdictionary dictionary.txt
-classobfuscationdictionary dictionary.txt

-ignorewarnings
-dontwarn
-dontnote

-dontwarn sun.misc.**
-dontwarn android.arch.**
-dontwarn android.lifecycle.**
-keep class android.arch.** { *; }
-keep class android.lifecycle.** { *; }
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-dontwarn androidx.arch.**
-dontwarn androidx.lifecycle.**
-keep class androidx.arch.** { *; }
-keep class androidx.lifecycle.** { *; }

-keepclasseswithmembernames,includedescriptorclasses class * { native <methods>; }
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder { *** rewind(); }

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
